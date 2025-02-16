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

package com.rwmobi.kunigami.data.repository

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
    private val fakeMpan = "mpan"
    private val fakeMeterSerialNumber = "meterSerialNumber"
    private val fakeAccountNumber = "B-1234A1A1"
    private val fakeDeviceId = "00-00-00-00-00-00-00-00"
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
                accountNumber = fakeAccountNumber,
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
            mpan = fakeMpan,
            period = start..now,
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
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
            mpan = fakeMpan,
            period = start..now,
            groupBy = ConsumptionTimeFrame.DAY,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
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
            mpan = fakeMpan,
            period = start..now,
            groupBy = ConsumptionTimeFrame.WEEK,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
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
            mpan = fakeMpan,
            period = start..now,
            groupBy = ConsumptionTimeFrame.MONTH,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
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
                mpan = fakeMpan,
                period = start..now,
                groupBy = ConsumptionTimeFrame.QUARTER,
                accountNumber = fakeAccountNumber,
                deviceId = fakeDeviceId,
            )
        }
    }
}
