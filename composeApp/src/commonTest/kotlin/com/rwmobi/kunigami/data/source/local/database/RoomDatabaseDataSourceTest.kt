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
package com.rwmobi.kunigami.data.source.local.database

import com.rwmobi.kunigami.data.source.local.database.dao.FakeConsumptionDao
import com.rwmobi.kunigami.data.source.local.database.dao.FakeRateDao
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.test.samples.ConsumptionEntitySampleData
import com.rwmobi.kunigami.test.samples.RateEntitySampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class RoomDatabaseDataSourceTest {

    private lateinit var consumptionDao: FakeConsumptionDao
    private lateinit var rateDao: FakeRateDao
    private lateinit var dataSource: RoomDatabaseDataSource

    @BeforeTest
    fun setup() {
        consumptionDao = FakeConsumptionDao()
        rateDao = FakeRateDao()
        dataSource = RoomDatabaseDataSource(consumptionDao, rateDao)
    }

    @Test
    fun `insertConsumption should add consumption to the database`() = runBlocking {
        val consumption = ConsumptionEntitySampleData.sample1
        dataSource.insertConsumption(consumption)

        val result = consumptionDao.getConsumptions(
            deviceId = consumption.deviceId,
            intervalStart = consumption.intervalStart,
            intervalEnd = consumption.intervalEnd,
        )

        assertEquals(expected = listOf(consumption), actual = result)
    }

    @Test
    fun `insertConsumptions should add consumptions to the database`() = runBlocking {
        val consumptions = listOf(ConsumptionEntitySampleData.sample1, ConsumptionEntitySampleData.sample2)
        dataSource.insertConsumptions(consumptions)

        val result = consumptionDao.getConsumptions(
            deviceId = consumptions[0].deviceId,
            intervalStart = consumptions[0].intervalStart,
            intervalEnd = consumptions[1].intervalEnd,
        )

        assertEquals(consumptions, result)
    }

    @Test
    fun `insertRate should add rate to the database`() = runBlocking {
        val rate = RateEntitySampleData.standingChargeSample1
        dataSource.insertRate(rate)

        val result = rateDao.getRates(
            tariffCode = rate.tariffCode,
            rateType = rate.rateType,
            paymentMethod = rate.paymentMethod,
            validFrom = rate.validFrom,
            validTo = rate.validTo,
        )

        assertEquals(expected = listOf(rate), actual = result)
    }

    @Test
    fun `insertRates should add rates to the database`() = runBlocking {
        val rates = listOf(RateEntitySampleData.standingChargeSample1, RateEntitySampleData.standingChargeSample2)
        dataSource.insertRates(rates)

        val result = rateDao.getRates(
            tariffCode = rates[0].tariffCode,
            rateType = rates[0].rateType,
            paymentMethod = rates[0].paymentMethod,
            validFrom = rates[0].validFrom,
            validTo = rates[1].validTo,
        )

        assertEquals(expected = rates, actual = result)
    }

    @Test
    fun `getConsumptions should return the correct consumptions`() = runBlocking {
        val deviceId = "01-02-03-04-05-06-07-08"
        val interval = Instant.parse("2023-06-01T01:00:00Z")..Instant.parse("2023-06-01T02:00:00Z")
        val expectedConsumptions = listOf(ConsumptionEntitySampleData.sample1, ConsumptionEntitySampleData.sample2)

        consumptionDao.insert(expectedConsumptions)

        val result = dataSource.getConsumptions(deviceId, interval)
        assertEquals(expected = expectedConsumptions, actual = result)
    }

    @Test
    fun `getRates should return the correct rates`() = runBlocking {
        val tariffCode = "E-1R-AGILE-24-04-03-A"
        val rateType = RateType.STANDING_CHARGE
        val paymentMethod = PaymentMethod.UNKNOWN
        val validity = RateEntitySampleData.standingChargeSample1.validFrom..Instant.DISTANT_FUTURE
        val expectedRates = listOf(RateEntitySampleData.standingChargeSample1, RateEntitySampleData.standingChargeSample2)

        rateDao.insert(expectedRates)

        val result = dataSource.getRates(
            tariffCode = tariffCode,
            rateType = rateType,
            validity = validity,
            paymentMethod = paymentMethod,
        )
        assertEquals(expected = expectedRates, actual = result)
    }

    @Test
    fun `clear should remove all data from the database`() = runBlocking {
        consumptionDao.insert(ConsumptionEntitySampleData.sample1)
        rateDao.insert(RateEntitySampleData.standingChargeSample1)

        dataSource.clear()

        assertTrue(
            consumptionDao.getConsumptions(
                deviceId = ConsumptionEntitySampleData.sample1.deviceId,
                intervalStart = Instant.DISTANT_PAST,
                intervalEnd = Instant.DISTANT_FUTURE,
            ).isEmpty(),
        )
        assertTrue(
            rateDao.getRates(
                tariffCode = RateEntitySampleData.standingChargeSample1.tariffCode,
                rateType = RateEntitySampleData.standingChargeSample1.rateType,
                paymentMethod = RateEntitySampleData.standingChargeSample1.paymentMethod,
                validFrom = Instant.DISTANT_PAST,
                validTo = Instant.DISTANT_FUTURE,
            ).isEmpty(),
        )
    }
}
