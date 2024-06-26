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
import com.rwmobi.kunigami.data.source.local.database.entity.StandingChargeEntity
import kotlinx.datetime.Instant

@Dao
interface StandingChargeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(standingChargeEntity: StandingChargeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(standingChargeEntity: List<StandingChargeEntity>)

    @Query(
        """
    SELECT * 
    FROM standing_charge 
    WHERE 
        tariff_code = :tariffCode 
        AND interval_start >= :intervalStart 
        AND interval_start < :intervalEnd 
        AND (:paymentMethod IS NULL OR payment_method = :paymentMethod) 
    ORDER BY interval_start ASC
    """,
    )
    suspend fun getStandingCharges(
        tariffCode: String,
        intervalStart: Instant,
        intervalEnd: Instant?,
        paymentMethod: String? = null,
    ): List<StandingChargeEntity>

    @Query("DELETE FROM standing_charge")
    suspend fun clear()
}
