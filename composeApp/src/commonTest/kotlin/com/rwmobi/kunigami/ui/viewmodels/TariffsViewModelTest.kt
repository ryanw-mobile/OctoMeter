/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.account.GetDefaultPostcodeUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetFilteredProductsUseCase
import com.rwmobi.kunigami.test.samples.GetProductsSampleData
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffsScreenType
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.viewmodels.TariffsViewModel
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
class TariffsViewModelTest {
    private lateinit var tariffsViewModel: TariffsViewModel
    private lateinit var octopusApiRepository: FakeOctopusApiRepository
    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository

    private val sampleProductCode = "AGILE-24-04-03"
    private val sampleProductDetails = ProductDetails(
        displayName = "Agile Octopus",
        code = "AGILE-24-04-03",
        direction = ProductDirection.IMPORT,
        fullName = "Agile Octopus April 2024 v1",
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        features = listOf(
            ProductFeature.VARIABLE,
            ProductFeature.GREEN,
            ProductFeature.CHARGEDHALFHOURLY,
        ),
        term = 12,
        availability = Instant.parse("2024-04-02T23:00:00Z")..Instant.DISTANT_FUTURE,
        electricityTariff = null,
        brand = "OCTOPUS_ENERGY",
    )

    @BeforeTest
    fun setupViewModel() {
        val dispatcher = UnconfinedTestDispatcher()
        octopusApiRepository = FakeOctopusApiRepository()
        userPreferencesRepository = FakeUserPreferencesRepository()

        tariffsViewModel = TariffsViewModel(
            octopusApiRepository = octopusApiRepository,
            getFilteredProductsUseCase = GetFilteredProductsUseCase(
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            getDefaultPostcodeUseCase = GetDefaultPostcodeUseCase(
                userPreferencesRepository = userPreferencesRepository,
                octopusApiRepository = octopusApiRepository,
                dispatcher = dispatcher,
            ),
            dispatcher = dispatcher,
        )

        // initialise a default screen type = List and Layout=Wide
        tariffsViewModel.notifyScreenSizeChanged(
            screenSizeInfo = ScreenSizeInfo(
                heightPx = 1280,
                widthPx = 1024,
                heightDp = 1280.dp,
                widthDp = 1024.dp,
            ),
            windowSizeClass = WindowSizeClass.calculateFromSize(
                size = Size(1280f, 1024f),
                density = Density(density = 2.0f),
            ),
        )
    }

    @Test
    fun `refresh should update UI state with postcode and products`() = runTest {
        val expectedPostcode = "WC1X 0ND"
        val productList = listOf(GetProductsSampleData.productSummaryPage1)
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setAccountResponse = Result.success(null)
        octopusApiRepository.setProductsResponse = Result.success(productList)

        tariffsViewModel.refresh()
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertEquals(expectedPostcode, uiState.queryPostCode)
        assertEquals(productList, uiState.productSummaries)
    }

    @Test
    fun `onQueryPostcode should update UI state with provided postcode`() = runTest {
        val providedPostcode = "AB12 3CD"
        val productList = listOf(GetProductsSampleData.productSummaryPage1)
        octopusApiRepository.setProductsResponse = Result.success(productList)

        tariffsViewModel.onQueryPostcode(providedPostcode)
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertEquals(providedPostcode, uiState.queryPostCode)
        assertEquals(productList, uiState.productSummaries)
    }

    @Test
    fun `getProductDetails should update UI state with product details`() = runTest {
        val postcode = "WC1X 0ND"
        octopusApiRepository.setProductDetailsResponse = Result.success(sampleProductDetails)

        tariffsViewModel.getProductDetails(
            productCode = sampleProductCode,
            postcode = postcode,
        )
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertEquals(sampleProductDetails, uiState.productDetails)
    }

    @Test
    fun `getProductDetails should handle error`() = runTest {
        val postcode = "WC1X 0ND"
        octopusApiRepository.setProductDetailsResponse =
            Result.failure(HttpException(httpStatusCode = 403))

        tariffsViewModel.getProductDetails(
            productCode = sampleProductCode,
            postcode = postcode,
        )
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is TariffsScreenType.Error)
        val errorScreen = uiState.requestedScreenType as TariffsScreenType.Error
        assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.HttpError)
        assertEquals(
            403,
            (errorScreen.specialErrorScreen as SpecialErrorScreen.HttpError).statusCode,
        )
    }

    @Test
    fun `errorShown should remove the error message with the given ID`() = runTest {
        userPreferencesRepository.demoMode = true
        val productList = listOf(GetProductsSampleData.productSummaryPage1)
        octopusApiRepository.setProductsResponse = Result.success(productList)
        val errorId = 1L

        tariffsViewModel.refresh() // Initialize with some state
        tariffsViewModel.errorShown(errorId)
        val uiState = tariffsViewModel.uiState.value

        assertTrue(uiState.errorMessages.none { it.id == errorId })
    }

    @Test
    fun `onSpecialErrorScreenShown should reset requestedScreenType`() = runTest {
        userPreferencesRepository.demoMode = true
        val productList = listOf(GetProductsSampleData.productSummaryPage1)
        octopusApiRepository.setProductsResponse = Result.success(productList)

        tariffsViewModel.refresh() // Initialize with some state
        tariffsViewModel.onSpecialErrorScreenShown()
        val uiState = tariffsViewModel.uiState.value

        assertNull(uiState.requestedScreenType)
    }

    @Test
    fun `onProductDetailsDismissed should clear product details`() = runTest {
        octopusApiRepository.setProductDetailsResponse = Result.success(sampleProductDetails)
        tariffsViewModel.getProductDetails(
            productCode = sampleProductCode,
            postcode = "WC1X 0ND",
        )

        tariffsViewModel.onProductDetailsDismissed()
        val uiState = tariffsViewModel.uiState.value

        assertNull(uiState.productDetails)
    }

    @Test
    fun `requestScrollToTop should update requestScrollToTop in UI state`() = runTest {
        tariffsViewModel.requestScrollToTop(true)

        val uiState = tariffsViewModel.uiState.value

        assertTrue(uiState.requestScrollToTop)
    }

    @Test
    fun `getFilteredProducts should handle HttpException failure`() = runTest {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setProductsResponse =
            Result.failure(HttpException(httpStatusCode = 403))

        tariffsViewModel.refresh()
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is TariffsScreenType.Error)
        val errorScreen = uiState.requestedScreenType as TariffsScreenType.Error
        assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.HttpError)
        assertEquals(
            403,
            (errorScreen.specialErrorScreen as SpecialErrorScreen.HttpError).statusCode,
        )
    }

    @Test
    fun `getFilteredProducts should handle UnresolvedAddressException failure`() = runTest {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setProductsResponse = Result.failure(UnresolvedAddressException())

        tariffsViewModel.refresh()
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is TariffsScreenType.Error)
        val errorScreen = uiState.requestedScreenType as TariffsScreenType.Error
        assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.NetworkError)
    }

    @Test
    fun `getFilteredProducts should handle generic Exception failure`() = runTest {
        userPreferencesRepository.demoMode = true
        val exceptionMessage = "Unexpected error occurred"
        octopusApiRepository.setProductsResponse = Result.failure(Exception(exceptionMessage))

        tariffsViewModel.refresh()
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertTrue(uiState.errorMessages.isNotEmpty())
        assertTrue(uiState.requestedScreenType is TariffsScreenType.List)

        val errorMessage = uiState.errorMessages.find { it.message == exceptionMessage }
        assertTrue(errorMessage != null) // Ensure the specific error message is included
    }

    @Test
    fun `getFilteredProducts should handle 401 Unauthorized failure`() = runTest {
        userPreferencesRepository.demoMode = true
        octopusApiRepository.setProductsResponse =
            Result.failure(HttpException(httpStatusCode = 401))

        tariffsViewModel.refresh()
        val uiState = tariffsViewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertTrue(uiState.requestedScreenType is TariffsScreenType.Error)

        val errorScreen = uiState.requestedScreenType as TariffsScreenType.Error
        assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.HttpError)
        assertEquals(
            401,
            (errorScreen.specialErrorScreen as SpecialErrorScreen.HttpError).statusCode,
        )
    }

    @Test
    fun `getFilteredProducts should handle 403 Forbidden failure with specific message`() =
        runTest {
            userPreferencesRepository.demoMode = true
            octopusApiRepository.setProductsResponse =
                Result.failure(HttpException(httpStatusCode = 403))

            tariffsViewModel.refresh()
            val uiState = tariffsViewModel.uiState.value

            assertFalse(uiState.isLoading)
            assertTrue(uiState.requestedScreenType is TariffsScreenType.Error)
            val errorScreen = uiState.requestedScreenType as TariffsScreenType.Error
            assertTrue(errorScreen.specialErrorScreen is SpecialErrorScreen.HttpError)
            assertEquals(
                403,
                (errorScreen.specialErrorScreen as SpecialErrorScreen.HttpError).statusCode,
            )
        }
}
