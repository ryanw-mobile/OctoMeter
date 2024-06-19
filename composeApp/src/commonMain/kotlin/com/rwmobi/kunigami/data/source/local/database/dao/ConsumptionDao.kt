/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import kotlinx.datetime.Instant

@Dao
interface ConsumptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(consumptionEntity: ConsumptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(consumptionEntity: List<ConsumptionEntity>)

    @Query("SELECT * FROM consumption WHERE meter_serial = :meterSerial AND interval_start >= :intervalStart AND interval_end<= :intervalEnd ORDER BY interval_start ASC")
    suspend fun getConsumptions(meterSerial: String, intervalStart: Instant, intervalEnd: Instant): List<ConsumptionEntity>

    @Query("DELETE FROM consumption")
    suspend fun clear()
}
