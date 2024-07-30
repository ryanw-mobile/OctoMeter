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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration

/***
 * The purpose of this test is to make sure we never put real implementation to this demo repository.
 * Tests will fail if we provide more implementation than we need for demo mode
 */
@Suppress("TooManyFunctions")
class DemoRestApiRepositoryTest {

    private val sampleTariffCode = "E-1R-SAMPLE-TARIFF-A"
    private val samplePostcode = "WC1X 0ND"
    private val demoRepository = DemoOctopusApiRepository()

    @Test
    fun `getSimpleProductTariff should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getTariff(tariffCode = sampleTariffCode)
        }
    }

    @Test
    fun `getProducts should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getProducts(postcode = samplePostcode)
        }
    }

    @Test
    fun `getProductDetails should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getProductDetails(
                productCode = "productCode",
                postcode = samplePostcode,
            )
        }
    }

    @Test
    fun `getStandardUnitRates should throw NotImplementedError`() = runTest {
        val now = Clock.System.now()
        val start = now - Duration.parse("30d")

        assertFailsWith<NotImplementedError> {
            demoRepository.getStandardUnitRates(
                tariffCode = sampleTariffCode,
                period = start..now,
                requestedPage = 1,
            )
        }
    }

    @Test
    fun `getStandingCharges should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getStandingCharges(
                tariffCode = sampleTariffCode,
                requestedPage = 1,
            )
        }
    }

    @Test
    fun `getDayUnitRates should throw NotImplementedError`() = runTest {
        val now = Clock.System.now()
        assertFailsWith<NotImplementedError> {
            demoRepository.getDayUnitRates(
                tariffCode = sampleTariffCode,
                period = now..now,
                requestedPage = 1,
            )
        }
    }

    @Test
    fun `getNightUnitRates should throw NotImplementedError`() = runTest {
        val now = Clock.System.now()
        assertFailsWith<NotImplementedError> {
            demoRepository.getNightUnitRates(
                tariffCode = sampleTariffCode,
                period = now..now,
                requestedPage = 1,
            )
        }
    }

    @Test
    fun `getAccount should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.getAccount(
                accountNumber = "accountNumber",
            )
        }
    }

    @Test
    fun `clearCache should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> {
            demoRepository.clearCache()
        }
    }

    @Test
    fun `getConsumption should generate random consumption data for HALF_HOURLY ConsumptionTimeFrame`() = runTest {
        val now = Clock.System.now()
        val start = now - Duration.parse("1d")

        val result = demoRepository.getConsumption(
            apiKey = "apiKey",
            mpan = "mpan",
            meterSerialNumber = "meterSerialNumber",
            period = start..now,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
            requestedPage = 1,
        )

        assertTrue(result.isSuccess)
        val consumptionList = result.getOrNull()
        assertEquals(48, consumptionList!!.size)
    }

    @Test
    fun `getConsumption should generate random consumption data for DAY ConsumptionTimeFrame`() = runTest {
        val now = Clock.System.now()
        val start = now - Duration.parse("30d")

        val result = demoRepository.getConsumption(
            apiKey = "apiKey",
            mpan = "mpan",
            meterSerialNumber = "meterSerialNumber",
            period = start..now,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = ConsumptionTimeFrame.DAY,
            requestedPage = 1,
        )

        assertTrue(result.isSuccess)
        val consumptionList = result.getOrNull()
        assertEquals(30, consumptionList!!.size)
    }

    @Test
    fun `getConsumption should generate random consumption data for WEEK ConsumptionTimeFrame`() = runTest {
        val now = Clock.System.now()
        val start = now - Duration.parse("7d")

        val result = demoRepository.getConsumption(
            apiKey = "apiKey",
            mpan = "mpan",
            meterSerialNumber = "meterSerialNumber",
            period = start..now,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = ConsumptionTimeFrame.WEEK,
            requestedPage = 1,
        )

        assertTrue(result.isSuccess)
        val consumptionList = result.getOrNull()
        assertEquals(1, consumptionList!!.size)
    }

    @Test
    fun `getConsumption should generate random consumption data for MONTH ConsumptionTimeFrame`() = runTest {
        val now = Clock.System.now()
        val start = now - Duration.parse("364d")

        val result = demoRepository.getConsumption(
            apiKey = "apiKey",
            mpan = "mpan",
            meterSerialNumber = "meterSerialNumber",
            period = start..now,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = ConsumptionTimeFrame.MONTH,
            requestedPage = 1,
        )

        assertTrue(result.isSuccess)
        val consumptionList = result.getOrNull()
        assertEquals(
            expected = 12,
            actual = consumptionList!!.size,
        )
    }

    @Test
    fun `getConsumption should throw NotImplementedError for QUARTER ConsumptionTimeFrame`() = runTest {
        val now = Clock.System.now()
        val start = now - Duration.parse("120d")

        assertFailsWith<NotImplementedError> {
            demoRepository.getConsumption(
                apiKey = "apiKey",
                mpan = "mpan",
                meterSerialNumber = "meterSerialNumber",
                period = start..now,
                orderBy = ConsumptionDataOrder.PERIOD,
                groupBy = ConsumptionTimeFrame.QUARTER,
                requestedPage = 1,
            )
        }
    }
}
