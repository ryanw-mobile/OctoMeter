/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.test.samples.RateSampleData
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
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository
    private val sampleTariffCode = "E-1R-SAMPLE-TARIFF-A"
    private val samplePeriod = Instant.parse("2023-01-01T00:00:00Z")..Instant.parse("2023-01-04T00:00:00Z")

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeOctopusApiRepository()
        getStandardUnitRateUseCase = GetStandardUnitRateUseCase(
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return sorted rates when repository returns rates`() = runTest {
        val rates = RateSampleData.rates
        fakeRestApiRepository.setStandardUnitRatesResponse = Result.success(rates)

        val result = getStandardUnitRateUseCase(
            tariffCode = sampleTariffCode,
            period = samplePeriod,
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
            tariffCode = sampleTariffCode,
            period = samplePeriod,
        )

        assertTrue(result.isFailure)
        assertEquals(exceptionMessage, result.exceptionOrNull()!!.message)
    }

    @Test
    fun `invoke should handle empty rate list`() = runTest {
        fakeRestApiRepository.setStandardUnitRatesResponse = Result.success(emptyList())

        val result = getStandardUnitRateUseCase(
            tariffCode = sampleTariffCode,
            period = samplePeriod,
        )

        assertTrue(result.isSuccess)
        val rates = result.getOrThrow()
        assertEquals(0, rates.size)
    }
}
