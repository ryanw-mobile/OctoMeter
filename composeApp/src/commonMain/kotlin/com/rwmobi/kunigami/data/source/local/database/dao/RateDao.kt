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
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import kotlinx.datetime.Instant

@Dao
interface RateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rateEntity: RateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rateEntity: List<RateEntity>)

    @Query(
        """
    SELECT * 
    FROM rate 
    WHERE 
        tariff_code = :tariffCode 
        AND rate_type = :rateType
        AND payment_method = :paymentMethod
        AND valid_from >= :validFrom 
        AND (:validTo IS NULL OR valid_from < :validTo)
    ORDER BY valid_from ASC
    """,
    )
    suspend fun getRates(
        tariffCode: String,
        rateType: RateType,
        paymentMethod: String,
        validFrom: Instant,
        validTo: Instant?,
    ): List<RateEntity>

    @Query("DELETE FROM rate")
    suspend fun clear()
}
