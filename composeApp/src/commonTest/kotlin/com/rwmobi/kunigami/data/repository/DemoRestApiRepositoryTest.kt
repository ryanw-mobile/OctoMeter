/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration

/***
 * The purpose of this test is to make sure we never put real implementation to this demo repository.
 * Tests will fail if we provide more implementation that we need for demo mode
 */
class DemoRestApiRepositoryTest {

    private val demoRepository = DemoRestApiRepository()
    private val now = Clock.System.now()
    private val start = now - Duration.parse("30d")
    private val end = now

    @Test
    fun `getSimpleProductTariff should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getSimpleProductTariff("productCode", "tariffCode")
        }
    }

    @Test
    fun `getProducts should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getProducts(1)
        }
    }

    @Test
    fun `getProductDetails should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getProductDetails("productCode")
        }
    }

    @Test
    fun `getStandardUnitRates should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getStandardUnitRates("productCode", "tariffCode", start..end, 1)
        }
    }

    @Test
    fun `getStandingCharges should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getStandingCharges("productCode", "tariffCode", 1)
        }
    }

    @Test
    fun `getDayUnitRates should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getDayUnitRates("productCode", "tariffCode", 1)
        }
    }

    @Test
    fun `getNightUnitRates should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getNightUnitRates("productCode", "tariffCode", 1)
        }
    }

    @Test
    fun `getAccount should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getAccount("apiKey", "accountNumber")
        }
    }

    @Test
    fun `getConsumption should generate random consumption data`() = runTest {
        val result = demoRepository.getConsumption(
            apiKey = "apiKey",
            mpan = "mpan",
            meterSerialNumber = "meterSerialNumber",
            period = start..end,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = ConsumptionTimeFrame.DAY,
            requestedPage = 1,
        )

        assertTrue(result.isSuccess)
        val consumptionList = result.getOrNull()
        assertTrue(consumptionList?.isNotEmpty() == true)

        // Validate that consumption values are within expected range
        consumptionList?.forEach { consumption ->
            assertTrue(consumption.kWhConsumed >= 0.05)
            assertTrue(consumption.kWhConsumed <= 1.5 * 24) // 24 hours in a day
        }
    }

    @Test
    fun `getConsumption should throw NotImplementedError for QUARTER ConsumptionTimeFrame`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getConsumption(
                apiKey = "apiKey",
                mpan = "mpan",
                meterSerialNumber = "meterSerialNumber",
                period = start..end,
                orderBy = ConsumptionDataOrder.PERIOD,
                groupBy = ConsumptionTimeFrame.QUARTER,
                requestedPage = 1,
            )
        }
    }
}
