/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.rwmobi.kunigami.data.source.local.database.dao.ConsumptionDao
import com.rwmobi.kunigami.data.source.local.database.dao.RateDao
import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object OctometerDatabaseCtor : RoomDatabaseConstructor<OctometerDatabase>

@Database(
    entities = [
        ConsumptionEntity::class,
        RateEntity::class,
    ],
    version = 3,
    exportSchema = true,
)
@TypeConverters(DatabaseTypeConverters::class)
@ConstructedBy(OctometerDatabaseCtor::class)
abstract class OctometerDatabase : RoomDatabase() {
    abstract val consumptionDao: ConsumptionDao
    abstract val rateDao: RateDao
}
