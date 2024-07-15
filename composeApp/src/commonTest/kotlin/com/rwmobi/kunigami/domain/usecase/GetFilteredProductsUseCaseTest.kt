/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetFilteredProductsUseCaseTest {

    private lateinit var getFilteredProductsUseCase: GetFilteredProductsUseCase
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeRestApiRepository = FakeOctopusApiRepository()
        getFilteredProductsUseCase = GetFilteredProductsUseCase(
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return filtered products successfully`() = runTest {
        val productSummaries = listOf(
            ProductSummary(
                code = "1",
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
                code = "2",
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
                code = "3",
                direction = ProductDirection.IMPORT,
                fullName = "Product 3",
                displayName = "Product 3",
                description = "Description 3",
                features = listOf(ProductFeature.BUSINESS),
                term = 12,
                availability = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-12-31T23:59:59Z"),
                brand = "OCTOPUS_ENERGY",
            ),
            ProductSummary(
                code = "4",
                direction = ProductDirection.IMPORT,
                fullName = "Product 4",
                displayName = "Product 4",
                description = "Description 4",
                features = listOf(ProductFeature.PREPAY),
                term = 12,
                availability = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-12-31T23:59:59Z"),
                brand = "OTHER_BRAND",
            ),
        )

        fakeRestApiRepository.setProductsResponse = Result.success(productSummaries)

        val result = getFilteredProductsUseCase()

        assertTrue(result.isSuccess)
        assertEquals(listOf(productSummaries[0]), result.getOrNull())
    }

    @Test
    fun `invoke should return failure result when repository call fails`() = runTest {
        val errorMessage = "API Error"
        fakeRestApiRepository.setProductsResponse = Result.failure(RuntimeException(errorMessage))

        val result = getFilteredProductsUseCase()

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is RuntimeException)
        assertEquals(errorMessage, exception!!.message)
    }

    @Test
    fun `invoke should return empty list when no products match criteria`() = runTest {
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

        val result = getFilteredProductsUseCase()

        assertTrue(result.isSuccess)
        assertEquals(emptyList<ProductSummary>(), result.getOrNull())
    }
}
