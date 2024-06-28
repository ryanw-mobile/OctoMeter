/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.data.source.local.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.kunigami.data.source.local.database.OctometerDatabase
import com.rwmobi.kunigami.test.samples.RateEntitySampleData
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class RateDaoTest {

    private lateinit var database: OctometerDatabase

    @BeforeTest
    fun setupDatabase() {
        // We don't need Koin in this test
        stopKoin()

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder<OctometerDatabase>(
            context = context,
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertAndRetrieveRateEntity() = runBlocking {
        val rate = RateEntitySampleData.standingChargeSample2
        database.rateDao.insert(rateEntity = rate)

        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standingChargeSample2.tariffCode,
            rateType = RateEntitySampleData.standingChargeSample2.rateType,
            paymentMethod = RateEntitySampleData.standingChargeSample2.paymentMethod,
            validFrom = RateEntitySampleData.standingChargeSample2.validFrom,
            validTo = RateEntitySampleData.standingChargeSample2.validTo,
        )

        assertEquals(expected = 1, actual = retrieved.size)
        assertEquals(expected = RateEntitySampleData.standingChargeSample2, actual = retrieved[0])
    }

    @Test
    fun insertAndRetrieveMultipleRateEntities() = runBlocking {
        val rates = listOf(
            RateEntitySampleData.standingChargeSample1,
            RateEntitySampleData.standingChargeSample2,
            RateEntitySampleData.standardUnitRateSample1,
            RateEntitySampleData.standardUnitRateSample2,
            RateEntitySampleData.standardUnitRateSample3,
        )
        database.rateDao.insert(rateEntity = rates)

        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standardUnitRateSample1.tariffCode,
            rateType = RateEntitySampleData.standardUnitRateSample1.rateType,
            paymentMethod = RateEntitySampleData.standardUnitRateSample1.paymentMethod,
            validFrom = RateEntitySampleData.standardUnitRateSample1.validFrom,
            validTo = RateEntitySampleData.standardUnitRateSample2.validTo,
        )

        assertEquals(expected = 2, actual = retrieved.size)
        assertEquals(expected = rates[2], actual = retrieved[0])
        assertEquals(expected = rates[3], actual = retrieved[1])
    }

    @Test
    fun getRates_ShouldReturnEmptyList_IfTariffCodeHasNoDataInDb() = runBlocking {
        val rate = RateEntitySampleData.standingChargeSample1
        database.rateDao.insert(rateEntity = rate)

        val retrieved = database.rateDao.getRates(
            tariffCode = "invalid-code",
            rateType = RateEntitySampleData.standingChargeSample1.rateType,
            paymentMethod = RateEntitySampleData.standingChargeSample1.paymentMethod,
            validFrom = RateEntitySampleData.standingChargeSample1.validFrom,
            validTo = RateEntitySampleData.standingChargeSample1.validTo,
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun getRates_ShouldReturnEmptyList_IfRateTypeHasDataButNotWithinRequestedRange() = runBlocking {
        val rate = RateEntitySampleData.standardUnitRateSample1
        database.rateDao.insert(rateEntity = rate)

        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standardUnitRateSample1.tariffCode,
            rateType = RateEntitySampleData.standardUnitRateSample1.rateType,
            paymentMethod = RateEntitySampleData.standardUnitRateSample1.paymentMethod,
            validFrom = RateEntitySampleData.standardUnitRateSample1.validTo!!,
            validTo = RateEntitySampleData.standardUnitRateSample1.validTo!!.plus(Duration.parse("1h")),
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun getRates_ShouldReturnOneEntry_IfValidFromAndValidToCoverOneOfThem() = runBlocking {
        val rates = listOf(
            RateEntitySampleData.standardUnitRateSample1,
            RateEntitySampleData.standardUnitRateSample2,
        )
        database.rateDao.insert(rateEntity = rates)

        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standardUnitRateSample1.tariffCode,
            rateType = RateEntitySampleData.standardUnitRateSample1.rateType,
            paymentMethod = RateEntitySampleData.standardUnitRateSample1.paymentMethod,
            validFrom = RateEntitySampleData.standardUnitRateSample1.validFrom,
            validTo = RateEntitySampleData.standardUnitRateSample1.validTo,
        )

        assertEquals(expected = 1, actual = retrieved.size)
        assertEquals(expected = rates[0], actual = retrieved[0])
    }

    @Test
    fun clearRateEntities() = runBlocking {
        val rate = RateEntitySampleData.standardUnitRateSample1
        database.rateDao.insert(rateEntity = rate)

        database.rateDao.clear()
        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standardUnitRateSample1.tariffCode,
            rateType = RateEntitySampleData.standardUnitRateSample1.rateType,
            paymentMethod = RateEntitySampleData.standardUnitRateSample1.paymentMethod,
            validFrom = RateEntitySampleData.standardUnitRateSample1.validFrom,
        )

        assertTrue(retrieved.isEmpty())
    }
}
