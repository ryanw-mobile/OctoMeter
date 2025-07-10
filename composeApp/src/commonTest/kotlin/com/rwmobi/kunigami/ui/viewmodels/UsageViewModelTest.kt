/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.viewmodels

import ConsumptionSampleData
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.data.repository.DemoOctopusApiRepository
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.account.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.consumption.GenerateUsageInsightsUseCase
import com.rwmobi.kunigami.domain.usecase.consumption.GetConsumptionAndCostUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetTariffUseCase
import com.rwmobi.kunigami.test.samples.RateSampleData
import com.rwmobi.kunigami.ui.destinations.usage.UsageScreenType
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.model.StubStringResourceProvider
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionQueryFilter
import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration

@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
class UsageViewModelTest {

    private lateinit var usageViewModel: UsageViewModel
    private lateinit var octopusApiRepository: FakeOctopusApiRepository
    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository

    @BeforeTest
    fun setupViewModel() {
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        octopusApiRepository = FakeOctopusApiRepository()
        userPreferencesRepository = FakeUserPreferencesRepository()

        usageViewModel = UsageViewModel(
            syncUserProfileUseCase = SyncUserProfileUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            getTariffUseCase = GetTariffUseCase(
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            getConsumptionAndCostUseCase = GetConsumptionAndCostUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                demoOctopusApiRepository = DemoOctopusApiRepository(),
                dispatcher = dispatcher,
            ),
            generateUsageInsightsUseCase = GenerateUsageInsightsUseCase(),
            stringResourceProvider = StubStringResourceProvider(),
            dispatcher = dispatcher,
        )

        usageViewModel.notifyScreenSizeChanged(
            screenSizeInfo = ScreenSizeInfo(
                heightPx = 1280,
                widthPx = 1024,
                heightDp = 1280.dp,
                widthDp = 1024.dp,
            ),
            windowSizeClass = WindowSizeClass.calculateFromSize(
                size = DpSize(
                    width = 1280.dp,
                    height = 1024.dp,
                ),
            ),
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupDemoMode() {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setSimpleProductTariffResponse = Result.success(TariffSamples.agileFlex221125)
        octopusApiRepository.setStandardUnitRatesResponse = Result.success(RateSampleData.rates)
    }

    private fun setupLoggedInMode() {
        val accountSample = AccountSamples.account928
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.apiKey = "sample-api-key"
        userPreferencesRepository.accountNumber = accountSample.accountNumber
        userPreferencesRepository.mpan = accountSample.electricityMeterPoints[0].mpan
        userPreferencesRepository.meterSerialNumber = accountSample.getDefaultMeterSerialNumber()
        octopusApiRepository.setAccountResponse = Result.success(accountSample)
        octopusApiRepository.setSimpleProductTariffResponse = Result.success(TariffSamples.agileFlex221125)
        octopusApiRepository.setStandardUnitRatesResponse = Result.success(RateSampleData.rates)
        octopusApiRepository.setConsumptionResponse = Result.success(ConsumptionSampleData.randomSample)
    }

    @Test
    fun `initialLoad should load user profile and consumption data successfully under demo mode`() = runTest {
        setupDemoMode()

        usageViewModel.initialLoad()
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.isDemoMode!!)
        assertTrue(uiState.consumptionGroupedCells.isNotEmpty())
        assertTrue(uiState.errorMessages.isEmpty())
        assertNotNull(uiState.userProfile)
        assertNotNull(uiState.barChartData)
        assertNotNull(uiState.consumptionQueryFilter)
        assertNotNull(uiState.insights)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertEquals(uiState.tariff, TariffSamples.agileFlex221125)
    }

    @Test
    fun `initialLoad should load user profile and consumption data successfully`() = runTest {
        setupLoggedInMode()

        usageViewModel.initialLoad()
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertFalse(uiState.isDemoMode!!)
        assertTrue(uiState.consumptionGroupedCells.isNotEmpty())
        assertTrue(uiState.errorMessages.isEmpty())
        assertNotNull(uiState.userProfile)
        assertNotNull(uiState.barChartData)
        assertNotNull(uiState.consumptionQueryFilter)
        assertNotNull(uiState.insights)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertEquals(uiState.tariff, TariffSamples.agileFlex221125)
    }

    @Test
    fun `initialLoad should return error message if failed to load consumption data`() = runTest {
        setupLoggedInMode()
        octopusApiRepository.setConsumptionResponse = Result.failure(RuntimeException("test exception"))

        usageViewModel.initialLoad()
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertNotNull(uiState.errorMessages)
    }

    @Test
    fun `initialLoad should return error message if failed to load standard unit rates`() = runTest {
        setupLoggedInMode()
        octopusApiRepository.setStandardUnitRatesResponse = Result.failure(RuntimeException("test exception"))

        usageViewModel.initialLoad()
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertNotNull(uiState.errorMessages)
    }

    @Test
    fun `initialLoad should handle HttpException`() = runTest {
        setupLoggedInMode()
        octopusApiRepository.setConsumptionResponse = Result.failure(HttpException(httpStatusCode = 403))

        usageViewModel.initialLoad()
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Error(SpecialErrorScreen.HttpError(statusCode = 403)), uiState.requestedScreenType)
    }

    @Test
    fun `initialLoad should handle UnresolvedAddressException`() = runTest {
        setupLoggedInMode()
        octopusApiRepository.setConsumptionResponse = Result.failure(UnresolvedAddressException())

        usageViewModel.initialLoad()
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Error(SpecialErrorScreen.NetworkError), uiState.requestedScreenType)
    }

    @Test
    fun `onSwitchPresentationStyle should switch the presentation style`() = runTest {
        setupDemoMode()
        usageViewModel.initialLoad()
        advanceUntilIdle()

        usageViewModel.onSwitchPresentationStyle(
            consumptionQueryFilter = usageViewModel.uiState.value.consumptionQueryFilter!!,
            presentationStyle = ConsumptionPresentationStyle.MONTH_WEEKS,
        )
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertEquals(ConsumptionPresentationStyle.MONTH_WEEKS, uiState.consumptionQueryFilter?.presentationStyle)
    }

    @Test
    fun `onPreviousTimeFrame should navigate to the previous time frame`() = runTest {
        setupDemoMode()
        usageViewModel.initialLoad()
        advanceUntilIdle()

        val currentFilter = usageViewModel.uiState.value.consumptionQueryFilter!!
        usageViewModel.onPreviousTimeFrame(consumptionQueryFilter = currentFilter)
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertTrue(uiState.consumptionQueryFilter?.requestedPeriod?.start!! < currentFilter.requestedPeriod.start)
        assertTrue(uiState.errorMessages.isEmpty())
    }

    @Test
    fun `onPreviousTimeFrame should return error message if failed to navigate`() = runTest {
        setupLoggedInMode()
        usageViewModel.initialLoad()
        advanceUntilIdle()

        val currentFilter = usageViewModel.uiState.value.consumptionQueryFilter!!.copy(
            referencePoint = AccountSamples.account928.movedInAt!!,
        )
        usageViewModel.onPreviousTimeFrame(consumptionQueryFilter = currentFilter)
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertNotNull(uiState.errorMessages)
    }

    @Test
    fun `onNextTimeFrame should navigate to the next time frame`() = runTest {
        setupDemoMode()
        usageViewModel.initialLoad()
        advanceUntilIdle()

        // Make sure it must be able to navigate forward
        val currentFilter = ConsumptionQueryFilter(
            presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
            referencePoint = Clock.System.now() - Duration.parse("2d"),
        )
        usageViewModel.onNextTimeFrame(consumptionQueryFilter = currentFilter)
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertTrue(uiState.consumptionQueryFilter?.requestedPeriod?.start!! > currentFilter.requestedPeriod.start)
        assertTrue(uiState.errorMessages.isEmpty())
    }

    @Test
    fun `onNextTimeFrame should return error message if failed to navigate`() = runTest {
        setupLoggedInMode()
        usageViewModel.initialLoad()
        advanceUntilIdle()

        val currentFilter = usageViewModel.uiState.value.consumptionQueryFilter!!.copy(
            referencePoint = Clock.System.now() + Duration.parse("2d"),
        )
        usageViewModel.onNextTimeFrame(consumptionQueryFilter = currentFilter)
        advanceUntilIdle()

        val uiState = usageViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(UsageScreenType.Chart, uiState.requestedScreenType)
        assertNotNull(uiState.errorMessages)
    }

    @Test
    fun `notifyScreenSizeChanged should update the UI state based on screen size`() = runTest {
        usageViewModel.notifyScreenSizeChanged(
            screenSizeInfo = ScreenSizeInfo(
                heightPx = 720,
                widthPx = 1280,
                heightDp = 320.dp,
                widthDp = 500.dp,
            ),
            windowSizeClass = WindowSizeClass.calculateFromSize(
                size = DpSize(
                    width = 500.dp,
                    height = 320.dp,
                ),
            ),
        )
        val uiState = usageViewModel.uiState.value
        assertEquals(WindowWidthSizeClass.Compact, uiState.requestedAdaptiveLayout)
    }

    @Test
    fun `errorShown should remove the error message with the given ID`() = runTest {
        setupLoggedInMode()
        octopusApiRepository.setAccountResponse = Result.failure(RuntimeException("test exception"))
        usageViewModel.initialLoad()
        advanceUntilIdle()

        val firstErrorMessageId = usageViewModel.uiState.value.errorMessages.first().id
        usageViewModel.errorShown(errorId = firstErrorMessageId)

        val uiState = usageViewModel.uiState.value
        assertNull(uiState.errorMessages.firstOrNull { it.id == firstErrorMessageId })
    }

    @Test
    fun `requestScrollToTop should update requestScrollToTop in UI state`() = runTest {
        usageViewModel.requestScrollToTop(true)
        val uiState = usageViewModel.uiState.value
        assertTrue(uiState.requestScrollToTop)
    }
}
