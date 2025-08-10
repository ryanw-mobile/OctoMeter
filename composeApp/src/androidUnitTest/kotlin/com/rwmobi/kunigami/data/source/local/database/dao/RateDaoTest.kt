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
package com.rwmobi.kunigami.data.source.local.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.kunigami.data.source.local.database.OctometerDatabase
import com.rwmobi.kunigami.test.samples.RateEntitySampleData
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = NoKoinTestApplication::class, sdk = [35])
internal class RateDaoTest {

    private lateinit var database: OctometerDatabase

    @BeforeTest
    fun setupDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder<OctometerDatabase>(
            context = context,
        ).allowMainThreadQueries().build()
    }

    @AfterTest
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertAndRetrieveRateEntity() = runBlocking {
        val sampleRate = RateEntitySampleData.standingChargeSample2
        database.rateDao.insert(rateEntity = sampleRate)

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
        val sampleRates = listOf(
            RateEntitySampleData.standingChargeSample1,
            RateEntitySampleData.standingChargeSample2,
            RateEntitySampleData.standardUnitRateSample1,
            RateEntitySampleData.standardUnitRateSample2,
            RateEntitySampleData.standardUnitRateSample3,
        )
        database.rateDao.insert(rateEntity = sampleRates)

        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standardUnitRateSample1.tariffCode,
            rateType = RateEntitySampleData.standardUnitRateSample1.rateType,
            paymentMethod = RateEntitySampleData.standardUnitRateSample1.paymentMethod,
            validFrom = RateEntitySampleData.standardUnitRateSample1.validFrom,
            validTo = RateEntitySampleData.standardUnitRateSample2.validTo,
        )

        assertEquals(expected = 2, actual = retrieved.size)
        assertEquals(expected = sampleRates[2], actual = retrieved[0])
        assertEquals(expected = sampleRates[3], actual = retrieved[1])
    }

    @Test
    fun getRates_ShouldReturnEmptyList_IfTariffCodeHasNoDataInDb() = runBlocking {
        val sampleRate = RateEntitySampleData.standingChargeSample1
        database.rateDao.insert(rateEntity = sampleRate)

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
        val sampleRate = RateEntitySampleData.standardUnitRateSample1
        database.rateDao.insert(rateEntity = sampleRate)

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
        val sampleRates = listOf(
            RateEntitySampleData.standardUnitRateSample1,
            RateEntitySampleData.standardUnitRateSample2,
        )
        database.rateDao.insert(rateEntity = sampleRates)

        val retrieved = database.rateDao.getRates(
            tariffCode = RateEntitySampleData.standardUnitRateSample1.tariffCode,
            rateType = RateEntitySampleData.standardUnitRateSample1.rateType,
            paymentMethod = RateEntitySampleData.standardUnitRateSample1.paymentMethod,
            validFrom = RateEntitySampleData.standardUnitRateSample1.validFrom,
            validTo = RateEntitySampleData.standardUnitRateSample1.validTo,
        )

        assertEquals(expected = 1, actual = retrieved.size)
        assertEquals(expected = sampleRates[0], actual = retrieved[0])
    }

    @Test
    fun clearRateEntities() = runBlocking {
        val sampleRate = RateEntitySampleData.standardUnitRateSample1
        database.rateDao.insert(rateEntity = sampleRate)

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
