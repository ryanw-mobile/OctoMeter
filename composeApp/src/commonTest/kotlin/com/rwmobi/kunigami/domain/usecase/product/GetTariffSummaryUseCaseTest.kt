/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase.product

import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.test.samples.TariffSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetTariffSummaryUseCaseTest {
    private lateinit var getTariffUseCase: GetTariffUseCase
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeRestApiRepository = FakeOctopusApiRepository()
        getTariffUseCase = GetTariffUseCase(
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return TariffSummary when repository returns success`() = runTest {
        val expectedTariffSummary = TariffSampleData.agileFlex221125
        val tariffCode = expectedTariffSummary.tariffCode
        fakeRestApiRepository.setSimpleProductTariffResponse = Result.success(expectedTariffSummary)

        val result = getTariffUseCase(tariffCode = tariffCode)

        assertTrue(result.isSuccess)
        assertEquals(expectedTariffSummary, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository returns failure`() = runTest {
        fakeRestApiRepository.setSimpleProductTariffResponse = Result.failure(Exception("Test Exception"))

        val result = getTariffUseCase("test_tariff_code")

        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }
}
