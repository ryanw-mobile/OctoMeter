/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.samples.RateSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetStandardUnitRateUseCaseTest {

    private lateinit var getStandardUnitRateUseCase: GetStandardUnitRateUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeRestApiRepository()
        getStandardUnitRateUseCase = GetStandardUnitRateUseCase(
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return sorted rates when repository returns rates`() = runTest {
        val rates = RateSampleData.rates
        fakeRestApiRepository.setStandardUnitRatesResponse = Result.success(rates)

        val result = getStandardUnitRateUseCase(
            productCode = "testProduct",
            tariffCode = "testTariff",
            period = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-01-04T00:00:00Z"),
        )

        assertTrue(result.isSuccess)
        val sortedRates = result.getOrThrow()
        assertEquals(rates.size, sortedRates.size)
        assertEquals(rates.sortedBy { it.validity.start }, sortedRates)
    }

    @Test
    fun `invoke should return failure when repository throws an exception`() = runTest {
        val exceptionMessage = "Test Exception"
        fakeRestApiRepository.setStandardUnitRatesResponse = Result.failure(RuntimeException(exceptionMessage))

        val result = getStandardUnitRateUseCase(
            productCode = "testProduct",
            tariffCode = "testTariff",
            period = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-01-04T00:00:00Z"),
        )

        assertTrue(result.isFailure)
        assertEquals(exceptionMessage, result.exceptionOrNull()!!.message)
    }

    @Test
    fun `invoke should handle empty rate list`() = runTest {
        fakeRestApiRepository.setStandardUnitRatesResponse = Result.success(emptyList())

        val result = getStandardUnitRateUseCase(
            productCode = "testProduct",
            tariffCode = "testTariff",
            period = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-01-04T00:00:00Z"),
        )

        assertTrue(result.isSuccess)
        val rates = result.getOrThrow()
        assertEquals(0, rates.size)
    }
}
