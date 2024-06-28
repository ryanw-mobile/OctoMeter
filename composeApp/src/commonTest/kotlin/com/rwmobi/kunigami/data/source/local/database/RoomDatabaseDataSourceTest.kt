/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
            meterSerial = consumption.meterSerial,
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
            meterSerial = consumptions[0].meterSerial,
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
        val meterSerial = "11A1234567"
        val interval = Instant.parse("2023-06-01T01:00:00Z")..Instant.parse("2023-06-01T02:00:00Z")
        val expectedConsumptions = listOf(ConsumptionEntitySampleData.sample1, ConsumptionEntitySampleData.sample2)

        consumptionDao.insert(expectedConsumptions)

        val result = dataSource.getConsumptions(meterSerial, interval)
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
                meterSerial = ConsumptionEntitySampleData.sample1.meterSerial,
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
