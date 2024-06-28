/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import com.rwmobi.kunigami.test.samples.TariffSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetTariffRatesUseCaseTest {

    private lateinit var getTariffRatesUseCase: GetTariffRatesUseCase
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeRestApiRepository = FakeRestApiRepository()
        getTariffRatesUseCase = GetTariffRatesUseCase(
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return tariff successfully when valid data is provided`() = runTest {
        val tariffCode = "E-1R-AGILE-FLEX-22-11-25-A"
        val expectedTariffSummary = TariffSampleData.agileFlex221125

        fakeRestApiRepository.setSimpleProductTariffResponse = Result.success(expectedTariffSummary)

        val result = getTariffRatesUseCase(tariffCode = tariffCode)

        assertTrue(result.isSuccess)
        assertEquals(expectedTariffSummary, result.getOrNull())
    }

    @Test
    fun `invoke should return failure result when repository call fails`() = runTest {
        val tariffCode = "E-1R-AGILE-FLEX-22-11-25-A"
        val errorMessage = "API Error"

        fakeRestApiRepository.setSimpleProductTariffResponse = Result.failure(RuntimeException(errorMessage))

        val result = getTariffRatesUseCase(tariffCode = tariffCode)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
}
