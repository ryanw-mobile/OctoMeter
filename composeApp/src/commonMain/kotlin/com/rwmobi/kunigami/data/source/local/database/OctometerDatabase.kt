/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rwmobi.kunigami.data.source.local.database.dao.ConsumptionDao
import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity

@Database(
    entities = [
        ConsumptionEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(InstantConverters::class)
abstract class OctometerDatabase : RoomDatabase() {
    abstract val consumptionDao: ConsumptionDao
}
