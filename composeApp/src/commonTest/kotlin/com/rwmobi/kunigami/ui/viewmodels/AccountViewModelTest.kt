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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import com.apollographql.apollo.exception.ApolloHttpException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.account.ClearCacheUseCase
import com.rwmobi.kunigami.domain.usecase.account.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.account.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.account.UpdateMeterPreferenceUseCase
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenType
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import com.rwmobi.kunigami.ui.viewmodels.AccountViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class AccountViewModelTest {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var octopusApiRepository: FakeOctopusApiRepository
    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository

    private val sampleAccountNumber = AccountSamples.account928.accountNumber
    private val sampleMpan = "9900000999999"
    private val sampleSerialNumber = "99A9999999"
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

        accountViewModel = AccountViewModel(
            userPreferencesRepository = userPreferencesRepository,
            initialiseAccountUseCase = InitialiseAccountUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            updateMeterPreferenceUseCase = UpdateMeterPreferenceUseCase(
                userPreferencesRepository = userPreferencesRepository,
                dispatcher = dispatcher,
            ),
            syncUserProfileUseCase = SyncUserProfileUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            clearCacheUseCase = ClearCacheUseCase(
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            dispatcher = dispatcher,
        )

        // initialise a default screen type
        accountViewModel.notifyWindowSizeClassChanged(
            windowSizeClass = WindowSizeClass.calculateFromSize(
                size = Size(
                    width = 1280f,
                    height = 1024f,
                ),
                density = Density(density = 2.0f),
            ),
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh should set onboarding screen type if under demo mode`() = runTest {
        userPreferencesRepository.demoMode = true

        accountViewModel.refresh()

        val uiState = accountViewModel.uiState.value
        assertEquals(AccountScreenType.Onboarding, uiState.requestedScreenType)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `refresh should set userProfile screen type if not under demo mode`() = runTest {
        userPreferencesRepository.demoMode = false
        val userProfile = sampleUserProfile
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber

        accountViewModel.refresh()

        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(userProfile, uiState.userProfile)
        assertEquals(AccountScreenType.Account, uiState.requestedScreenType)
    }

    @Test
    fun `refresh should handle error and update UI state accordingly`() = runTest {
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.accountNumber = sampleAccountNumber
        octopusApiRepository.setAccountResponse = Result.failure(
            ApolloHttpException(
                statusCode = 403,
                headers = emptyList(),
                body = null,
                message = "Test Exception",
            ),
        )

        accountViewModel.refresh()

        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is AccountScreenType.Error)
        val errorScreen = uiState.requestedScreenType as AccountScreenType.Error
        assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.HttpError)
        assertEquals(
            403,
            (errorScreen.specialErrorScreen as SpecialErrorScreen.HttpError).statusCode,
        )
    }

    @Test
    fun `onSpecialErrorScreenShown should set requestedScreenType to null`() = runTest {
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.accountNumber = sampleAccountNumber
        octopusApiRepository.setAccountResponse = Result.failure(
            ApolloHttpException(
                statusCode = 403,
                headers = emptyList(),
                body = null,
                message = "Test Exception",
            ),
        )
        accountViewModel.refresh()

        accountViewModel.onSpecialErrorScreenShown()

        assertNull(accountViewModel.uiState.value.requestedScreenType)
    }

    @Test
    fun `errorShown should remove error message by ID`() = runTest {
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.accountNumber = sampleAccountNumber
        val exceptionMessage = "Test Error"
        octopusApiRepository.setAccountResponse = Result.failure(
            Exception(exceptionMessage),
        )
        accountViewModel.refresh()
        val errorMessage = accountViewModel.uiState.value.errorMessages.firstOrNull()
        assertNotNull(errorMessage)
        assertEquals(exceptionMessage, errorMessage.message)

        accountViewModel.errorShown(errorId = errorMessage.id)

        assertTrue(accountViewModel.uiState.value.errorMessages.isEmpty())
    }

    @Test
    fun `clearCredentials should clear credentials and refresh`() = runTest {
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.apiKey = "test_api_key"
        val userProfile = sampleUserProfile
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber
        accountViewModel.refresh()

        accountViewModel.clearCredentials()

        assertTrue(userPreferencesRepository.isDemoMode())
        assertNull(userPreferencesRepository.getApiKey())
        assertNull(userPreferencesRepository.accountNumber)
        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(AccountScreenType.Onboarding, uiState.requestedScreenType)
    }

    @Test
    fun `submitCredentials should update credentials and refresh on success`() = runTest {
        val exceptionMessage = "exceptionMessage"
        val newApiKey = "new_api_key"
        val userProfile = sampleUserProfile
        userPreferencesRepository.demoMode = false
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber
        userPreferencesRepository.apiKey = "test_api_key"
        accountViewModel.refresh()

        accountViewModel.submitCredentials(apiKey = newApiKey, accountNumber = sampleAccountNumber) { exceptionMessage }

        val uiState = accountViewModel.uiState.value
        assertEquals(userProfile, uiState.userProfile)
        assertEquals(newApiKey, userPreferencesRepository.apiKey)
        assertEquals(sampleAccountNumber, userPreferencesRepository.accountNumber)
        assertEquals(AccountScreenType.Account, uiState.requestedScreenType)
    }

    @Test
    fun `submitCredentials should handle failure and update UI state`() = runTest {
        val exceptionMessage = "exceptionMessage"
        userPreferencesRepository.demoMode = false
        val userProfile = sampleUserProfile
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber
        accountViewModel.refresh()

        val newApiKey = "new_api_key"
        octopusApiRepository.setAccountResponse = Result.failure(
            ApolloHttpException(
                statusCode = 403,
                headers = emptyList(),
                body = null,
                message = "Test Exception",
            ),
        )
        accountViewModel.submitCredentials(apiKey = newApiKey, accountNumber = sampleAccountNumber) { exceptionMessage }

        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is AccountScreenType.Account)
        val errorMessage = accountViewModel.uiState.value.errorMessages.firstOrNull()
        assertNotNull(errorMessage)
        assertEquals(exceptionMessage, errorMessage.message)
    }

    @Test
    fun `updateMeterSerialNumber should update meter preferences and refresh on success`() = runTest {
        val mpan = sampleMpan
        val meterSerialNumber = sampleSerialNumber
        val userProfile = sampleUserProfile
        userPreferencesRepository.demoMode = false
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber
        userPreferencesRepository.apiKey = "test_api_key"
        accountViewModel.refresh()

        accountViewModel.updateMeterSerialNumber(mpan, meterSerialNumber)

        val uiState = accountViewModel.uiState.value
        assertEquals(mpan, userPreferencesRepository.mpan)
        assertEquals(meterSerialNumber, userPreferencesRepository.meterSerialNumber)
        assertEquals(AccountScreenType.Account, uiState.requestedScreenType)
    }

    @Test
    fun `updateMeterSerialNumber should handle failure and update UI state`() = runTest {
        val mpan = sampleMpan
        val meterSerialNumber = sampleSerialNumber
        val userProfile = sampleUserProfile
        userPreferencesRepository.demoMode = false
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber
        userPreferencesRepository.apiKey = "test_api_key"
        accountViewModel.refresh()

        octopusApiRepository.setAccountResponse = Result.failure(
            ApolloHttpException(
                statusCode = 403,
                headers = emptyList(),
                body = null,
                message = "Test Exception",
            ),
        )
        accountViewModel.updateMeterSerialNumber(mpan, meterSerialNumber)

        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is AccountScreenType.Error)
        val errorScreen = uiState.requestedScreenType as AccountScreenType.Error
        assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.HttpError)
        assertEquals(
            403,
            (errorScreen.specialErrorScreen as SpecialErrorScreen.HttpError).statusCode,
        )
    }

    @Test
    fun `onClearCache should clear cache and update UI state on success`() = runTest {
        val exceptionMessage = "Cache clearing failed"
        val userProfile = sampleUserProfile
        userPreferencesRepository.demoMode = false
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        userPreferencesRepository.accountNumber = sampleAccountNumber
        userPreferencesRepository.apiKey = "test_api_key"
        accountViewModel.refresh()

        accountViewModel.onClearCache(stringResolver = { exceptionMessage })

        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(AccountScreenType.Account, uiState.requestedScreenType)
    }

    @Test
    fun `onClearCache should handle failure and update UI state`() = runTest {
        val exceptionMessage = "Cache clearing failed"
        userPreferencesRepository.demoMode = false
        userPreferencesRepository.accountNumber = sampleAccountNumber
        val userProfile = sampleUserProfile
        octopusApiRepository.setAccountResponse = Result.success(userProfile.account)
        accountViewModel.refresh()

        octopusApiRepository.setClearCacheException = Exception(exceptionMessage)
        accountViewModel.onClearCache(stringResolver = { exceptionMessage })

        val uiState = accountViewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.errorMessages.any { it.message.contains(exceptionMessage) })
    }

    @Test
    fun `requestScrollToTop should update UI state with scroll request`() = runTest {
        accountViewModel.requestScrollToTop(true)

        val uiState = accountViewModel.uiState.value
        assertTrue(uiState.requestScrollToTop)
    }
}
