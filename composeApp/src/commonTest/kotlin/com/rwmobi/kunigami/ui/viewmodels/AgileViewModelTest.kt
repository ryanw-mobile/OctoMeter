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

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.account.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.consumption.GetLiveConsumptionUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetLatestProductByKeywordUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetTariffRatesUseCase
import com.rwmobi.kunigami.test.samples.GetProductsSampleData
import com.rwmobi.kunigami.test.samples.RateSampleData
import com.rwmobi.kunigami.test.samples.TariffSampleData
import com.rwmobi.kunigami.ui.destinations.agile.AgileScreenType
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.model.StubStringResourceProvider
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.product.RetailRegion
import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import com.rwmobi.kunigami.ui.viewmodels.AgileViewModel
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

@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
class AgileViewModelTest {
    private lateinit var agileViewModel: AgileViewModel
    private lateinit var octopusApiRepository: FakeOctopusApiRepository
    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository

    private val sampleAccountNumber = AccountSamples.account928.accountNumber
    private val sampleUserProfile = UserProfile(
        selectedMpan = AccountSamples.account928.electricityMeterPoints[0].mpan,
        selectedMeterSerialNumber = AccountSamples.account928.electricityMeterPoints[0].meters[0].serialNumber,
        account = AccountSamples.account928,
    )

    @BeforeTest
    fun setupViewModel() {
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        octopusApiRepository = FakeOctopusApiRepository()
        userPreferencesRepository = FakeUserPreferencesRepository()

        agileViewModel = AgileViewModel(
            getLatestProductByKeywordUseCase = GetLatestProductByKeywordUseCase(
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            getTariffRatesUseCase = GetTariffRatesUseCase(
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            getStandardUnitRateUseCase = GetStandardUnitRateUseCase(
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            syncUserProfileUseCase = SyncUserProfileUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            getLiveConsumptionUseCase = GetLiveConsumptionUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            stringResourceProvider = StubStringResourceProvider(),
            dispatcher = dispatcher,
        )

        // initialise a default screen type
        agileViewModel.notifyScreenSizeChanged(
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

    @Test
    fun `refresh should load user profile and set region correctly`() = runTest {
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.accountNumber = sampleAccountNumber
        userPreferencesRepository.apiKey = "sample-api-key"
        octopusApiRepository.setAccountResponse = Result.success(sampleUserProfile.account)
        octopusApiRepository.setProductsResponse = Result.success(listOf(GetProductsSampleData.productSummaryPage1))
        octopusApiRepository.setSimpleProductTariffResponse = Result.success(TariffSampleData.var221101)
        octopusApiRepository.setStandardUnitRatesResponse = Result.success(RateSampleData.rates)

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertFalse(uiState.isDemoMode!!)
        assertNotNull(uiState.userProfile)
        assertEquals(
            RetailRegion.LONDON,
            uiState.userProfile!!.getSelectedElectricityMeterPoint()?.getLatestAgreement()?.tariffCode?.let {
                Tariff.getRetailRegion(it)
            },
        )
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `refresh should fallback to demo mode on incomplete credentials`() = runTest {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setAccountResponse = Result.failure(IncompleteCredentialsException())
        octopusApiRepository.setProductsResponse = Result.success(listOf(GetProductsSampleData.productSummaryPage1))
        octopusApiRepository.setSimpleProductTariffResponse = Result.success(TariffSampleData.var221101)
        octopusApiRepository.setStandardUnitRatesResponse = Result.success(RateSampleData.rates)

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.isDemoMode!!)
    }

    @Test
    fun `refresh should load agile rates and update UI state`() = runTest {
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.accountNumber = sampleAccountNumber
        userPreferencesRepository.apiKey = "sample-api-key"
        octopusApiRepository.setAccountResponse = Result.success(sampleUserProfile.account)
        octopusApiRepository.setProductsResponse = Result.success(listOf(GetProductsSampleData.productSummaryPage1))
        octopusApiRepository.setSimpleProductTariffResponse = Result.success(TariffSampleData.var221101)
        octopusApiRepository.setStandardUnitRatesResponse = Result.success(RateSampleData.rates)

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.barChartData)
        assertEquals(AgileScreenType.Chart, uiState.requestedScreenType)
    }

    @Test
    fun `refresh should handle tariff rate retrieval failure`() = runTest {
        octopusApiRepository.setStandardUnitRatesResponse = Result.failure(Exception("Test Exception"))

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.barChartData)
        assertTrue(uiState.errorMessages.isNotEmpty())
    }

    @Test
    fun `refresh should propagate error message`() = runTest {
        val exceptionMessage = "Unexpected error occurred"
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setAccountResponse = Result.failure(Exception(exceptionMessage))
        octopusApiRepository.setProductsResponse = Result.failure(Exception(exceptionMessage))
        octopusApiRepository.setSimpleProductTariffResponse = Result.failure(Exception(exceptionMessage))
        octopusApiRepository.setStandardUnitRatesResponse = Result.failure(Exception(exceptionMessage))

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.errorMessages.isNotEmpty())
        assertTrue(uiState.requestedScreenType is AgileScreenType.Chart)

        val errorMessage = uiState.errorMessages.find { it.message == exceptionMessage }
        assertTrue(errorMessage != null) // Ensure the specific error message is included
    }

    @Test
    fun `refresh should handle HttpException`() = runTest {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setAccountResponse = Result.failure(HttpException(httpStatusCode = 403))
        octopusApiRepository.setProductsResponse = Result.failure(HttpException(httpStatusCode = 403))
        octopusApiRepository.setSimpleProductTariffResponse = Result.failure(HttpException(httpStatusCode = 403))
        octopusApiRepository.setStandardUnitRatesResponse = Result.failure(HttpException(httpStatusCode = 403))

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(AgileScreenType.Error(SpecialErrorScreen.HttpError(statusCode = 403)), uiState.requestedScreenType)
    }

    @Test
    fun `refresh should handle UnresolvedAddressException`() = runTest {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setAccountResponse = Result.failure(UnresolvedAddressException())
        octopusApiRepository.setProductsResponse = Result.failure(UnresolvedAddressException())
        octopusApiRepository.setSimpleProductTariffResponse = Result.failure(UnresolvedAddressException())
        octopusApiRepository.setStandardUnitRatesResponse = Result.failure(UnresolvedAddressException())

        agileViewModel.refresh()
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(AgileScreenType.Error(SpecialErrorScreen.NetworkError), uiState.requestedScreenType)
    }

    @Test
    fun `notifyScreenSizeChanged should update layout based on screen size`() {
        val newScreenSizeInfo = ScreenSizeInfo(
            heightPx = 320,
            widthPx = 240,
            heightDp = 320.dp,
            widthDp = 240.dp,
        )

        agileViewModel.notifyScreenSizeChanged(
            screenSizeInfo = newScreenSizeInfo,
            windowSizeClass = WindowSizeClass.calculateFromSize(
                size = DpSize(
                    width = 240.dp,
                    height = 320.dp,
                ),
            ),
        )

        val uiState = agileViewModel.uiState.value
        assertEquals(WindowWidthSizeClass.Compact, uiState.requestedAdaptiveLayout)
        assertTrue(uiState.requestedChartLayout is RequestedChartLayout.Portrait)
    }

    @Test
    fun `requestScrollToTop should update UI state`() = runTest {
        agileViewModel.requestScrollToTop(true)

        val uiState = agileViewModel.uiState.value
        assertTrue(uiState.requestScrollToTop)
    }

    @Test
    fun `getAgileRates should handle empty rates`() = runTest {
        octopusApiRepository.setStandardUnitRatesResponse = Result.success(emptyList())

        agileViewModel.refresh() // This should call getAgileRates internally
        advanceUntilIdle()

        val uiState = agileViewModel.uiState.value
        assertNotNull(uiState.rateRange)
        assertEquals(0.0..0.0, uiState.rateRange)
    }

    @Test
    fun `errorShown should remove the error message with the given ID`() = runTest {
        val exceptionMessage = "Unexpected error occurred"
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setAccountResponse = Result.failure(Exception(exceptionMessage))
        octopusApiRepository.setProductsResponse = Result.failure(Exception(exceptionMessage))
        octopusApiRepository.setSimpleProductTariffResponse = Result.failure(Exception(exceptionMessage))
        octopusApiRepository.setStandardUnitRatesResponse = Result.failure(Exception(exceptionMessage))

        agileViewModel.refresh() // Initialize with some state
        val errorMessage = agileViewModel.uiState.value.errorMessages.find { it.message == exceptionMessage }
        agileViewModel.errorShown(errorMessage!!.id)

        val uiState = agileViewModel.uiState.value
        assertTrue(uiState.errorMessages.none { it.id == errorMessage.id })
    }
}
