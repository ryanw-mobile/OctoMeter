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
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
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
        paymentMethod: PaymentMethod,
        validFrom: Instant,
        validTo: Instant? = null,
    ): List<RateEntity>

    @Query("DELETE FROM rate")
    suspend fun clear()
}
