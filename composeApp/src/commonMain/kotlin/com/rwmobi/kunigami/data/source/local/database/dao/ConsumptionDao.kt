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

    @Query(
        """
    SELECT * 
    FROM consumption 
    WHERE 
        device_id = :deviceId
        AND interval_start >= :intervalStart 
        AND interval_start < :intervalEnd 
    ORDER BY interval_start ASC
        """,
    )
    suspend fun getConsumptions(deviceId: String, intervalStart: Instant, intervalEnd: Instant): List<ConsumptionEntity>

    @Query("DELETE FROM consumption")
    suspend fun clear()
}
