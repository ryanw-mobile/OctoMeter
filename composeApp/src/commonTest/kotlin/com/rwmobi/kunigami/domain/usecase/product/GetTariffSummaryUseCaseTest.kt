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
