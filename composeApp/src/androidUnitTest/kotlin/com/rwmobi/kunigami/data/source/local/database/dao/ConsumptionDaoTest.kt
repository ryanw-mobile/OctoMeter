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
import com.rwmobi.kunigami.test.samples.ConsumptionEntitySampleData
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
@Config(application = NoKoinTestApplication::class)
internal class ConsumptionDaoTest {

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
    fun insertAndRetrieveConsumptionEntity() = runBlocking {
        val sampleConsumption = ConsumptionEntitySampleData.sample1
        database.consumptionDao.insert(sampleConsumption)

        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = ConsumptionEntitySampleData.sample1.deviceId,
            intervalStart = ConsumptionEntitySampleData.sample1.intervalStart - Duration.parse("1h"),
            intervalEnd = ConsumptionEntitySampleData.sample1.intervalStart + Duration.parse("1h"),
        )

        assertEquals(1, retrieved.size)
        assertEquals(ConsumptionEntitySampleData.sample1, retrieved[0])
    }

    @Test
    fun insertAndRetrieveMultipleConsumptionEntities() = runBlocking {
        val sampleConsumptions = listOf(
            ConsumptionEntitySampleData.sample1,
            ConsumptionEntitySampleData.sample2,
        )
        database.consumptionDao.insert(sampleConsumptions)

        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = ConsumptionEntitySampleData.sample1.deviceId,
            intervalStart = ConsumptionEntitySampleData.sample1.intervalStart - Duration.parse("1h"),
            intervalEnd = ConsumptionEntitySampleData.sample1.intervalStart + Duration.parse("1h"),
        )

        assertEquals(2, retrieved.size)
        assertEquals(sampleConsumptions[0], retrieved[0])
        assertEquals(sampleConsumptions[1], retrieved[1])
    }

    @Test
    fun getConsumptions_ShouldReturnEmptyList_IfMeterSerialHasNoDataInDb() = runBlocking {
        val sampleConsumption = ConsumptionEntitySampleData.sample1
        database.consumptionDao.insert(sampleConsumption)

        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = "invalid-device-id",
            intervalStart = ConsumptionEntitySampleData.sample1.intervalStart,
            intervalEnd = ConsumptionEntitySampleData.sample1.intervalEnd,
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun getConsumptions_ShouldReturnEmptyList_IfMeterSerialHasDataButNotWithinRequestedRange() = runBlocking {
        val sampleConsumption = ConsumptionEntitySampleData.sample1
        database.consumptionDao.insert(sampleConsumption)

        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = ConsumptionEntitySampleData.sample1.deviceId,
            intervalStart = ConsumptionEntitySampleData.sample1.intervalEnd,
            intervalEnd = ConsumptionEntitySampleData.sample1.intervalEnd + Duration.parse("1h"),
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun getConsumptions_ShouldReturnOneEntry_IfIntervalStartAndIntervalEndCoverOneOfThem() = runBlocking {
        val sampleConsumptions = listOf(
            ConsumptionEntitySampleData.sample1,
            ConsumptionEntitySampleData.sample2,
        )
        database.consumptionDao.insert(sampleConsumptions)

        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = ConsumptionEntitySampleData.sample1.deviceId,
            intervalStart = ConsumptionEntitySampleData.sample1.intervalStart,
            intervalEnd = ConsumptionEntitySampleData.sample1.intervalEnd,
        )

        assertEquals(1, retrieved.size)
        assertEquals(sampleConsumptions[0], retrieved[0])
    }

    @Test
    fun getConsumptions_ShouldReturnEmpty_WhenStartEqualsEnd() = runBlocking {
        val sample = ConsumptionEntitySampleData.sample1
        database.consumptionDao.insert(sample)

        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = sample.deviceId,
            intervalStart = sample.intervalStart,
            intervalEnd = sample.intervalStart, // same
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun clearConsumptionEntities() = runBlocking {
        val sampleConsumption = ConsumptionEntitySampleData.sample1
        database.consumptionDao.insert(sampleConsumption)

        database.consumptionDao.clear()
        val retrieved = database.consumptionDao.getConsumptions(
            deviceId = ConsumptionEntitySampleData.sample1.deviceId,
            intervalStart = ConsumptionEntitySampleData.sample1.intervalStart - Duration.parse("1h"),
            intervalEnd = ConsumptionEntitySampleData.sample1.intervalStart + Duration.parse("1h"),
        )

        assertTrue(retrieved.isEmpty())
    }
}
