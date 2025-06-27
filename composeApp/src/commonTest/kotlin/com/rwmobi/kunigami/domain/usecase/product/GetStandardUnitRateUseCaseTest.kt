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

package com.rwmobi.kunigami.domain.usecase.product

import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.test.samples.RateSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

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
