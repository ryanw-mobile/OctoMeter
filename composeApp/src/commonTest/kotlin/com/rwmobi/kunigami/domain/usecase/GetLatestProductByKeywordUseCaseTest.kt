package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class GetLatestProductByKeywordUseCaseTest {

    private lateinit var getLatestProductByKeywordUseCase: GetLatestProductByKeywordUseCase
    private lateinit var fakeRestApiRepository: FakeRestApiRepository
    private val keyword = "AGILE"

    @BeforeTest
    fun setupUseCase() {
        fakeRestApiRepository = FakeRestApiRepository()
        getLatestProductByKeywordUseCase = GetLatestProductByKeywordUseCase(
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `GetLatestProductByKeywordUseCase should return tariff code successfully`() = runTest {
        val productSummaries = listOf(
            ProductSummary(
                code = "GO-VAR-22-10-14",
                direction = ProductDirection.IMPORT,
                fullName = "Product 1",
                displayName = "Product 1",
                description = "Description 1",
                features = listOf(ProductFeature.GREEN),
                term = 12,
                availability = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-12-31T23:59:59Z"),
                brand = "OCTOPUS_ENERGY",
            ),
            ProductSummary(
                code = "AGILE-24-04-03",
                direction = ProductDirection.EXPORT,
                fullName = "Product 2",
                displayName = "Product 2",
                description = "Description 2",
                features = listOf(ProductFeature.VARIABLE),
                term = 12,
                availability = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-12-31T23:59:59Z"),
                brand = "OCTOPUS_ENERGY",
            ),
            ProductSummary(
                code = "PREPAY-VAR-18-90-21",
                direction = ProductDirection.IMPORT,
                fullName = "Product 3",
                displayName = "Product 3",
                description = "Description 3",
                features = listOf(ProductFeature.BUSINESS),
                term = 12,
                availability = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-12-31T23:59:59Z"),
                brand = "OCTOPUS_ENERGY",
            ),
        )

        fakeRestApiRepository.setProductsResponse = Result.success(productSummaries)

        val result = getLatestProductByKeywordUseCase(keyword = keyword)

        assertEquals(
            expected = "AGILE-24-04-03",
            actual = result,
        )
    }

    @Test
    fun `GetLatestProductByKeywordUseCase should return latest tariff code if more than one Agile products exist`() = runTest {
        val productSummaries = listOf(
            ProductSummary(
                code = "AGILE-FLEX-22-11-25",
                direction = ProductDirection.EXPORT,
                fullName = "Agile Octopus November 2022 v1",
                displayName = "Agile Octopus",
                description = "Sample description",
                features = listOf(ProductFeature.VARIABLE),
                term = 12,
                availability = Instant.parse("2022-11-25T00:00:00Z")..Instant.parse("2023-12-11T12:00:00Z"),
                brand = "OCTOPUS_ENERGY",
            ),
            ProductSummary(
                code = "AGILE-24-04-03",
                direction = ProductDirection.EXPORT,
                fullName = "Agile Octopus April 2024 v1",
                displayName = "Agile Octopus",
                description = "Sample description",
                features = listOf(ProductFeature.VARIABLE),
                term = 12,
                availability = Instant.parse("2024-04-03T00:00:00+01:00")..Instant.DISTANT_FUTURE,
                brand = "OCTOPUS_ENERGY",
            ),
        )

        fakeRestApiRepository.setProductsResponse = Result.success(productSummaries)

        val result = getLatestProductByKeywordUseCase(keyword = keyword)

        assertEquals(
            expected = "AGILE-24-04-03",
            actual = result,
        )
    }

    @Test
    fun `GetLatestProductByKeywordUseCase should return null when repository call fails`() = runTest {
        val errorMessage = "API Error"
        fakeRestApiRepository.setProductsResponse = Result.failure(RuntimeException(errorMessage))

        val result = getLatestProductByKeywordUseCase(keyword = keyword)

        assertNull(result)
    }

    @Test
    fun `GetLatestProductByKeywordUseCase should return null when no products match criteria`() = runTest {
        val productSummaries = listOf(
            ProductSummary(
                code = "1",
                direction = ProductDirection.EXPORT,
                fullName = "Product 1",
                displayName = "Product 1",
                description = "Description 1",
                features = listOf(ProductFeature.BUSINESS),
                term = 12,
                availability = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-12-31T23:59:59Z"),
                brand = "OTHER_BRAND",
            ),
        )
        fakeRestApiRepository.setProductsResponse = Result.success(productSummaries)

        val result = getLatestProductByKeywordUseCase(keyword = keyword)

        assertNull(result)
    }
}
