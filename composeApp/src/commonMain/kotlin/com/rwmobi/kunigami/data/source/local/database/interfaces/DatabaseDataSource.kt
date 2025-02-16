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

package com.rwmobi.kunigami.data.source.local.database.interfaces

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant

interface DatabaseDataSource {
    suspend fun insertConsumption(consumptionEntity: ConsumptionEntity)
    suspend fun insertConsumptions(consumptionEntity: List<ConsumptionEntity>)
    suspend fun getConsumptions(
        deviceId: String,
        interval: ClosedRange<Instant>,
    ): List<ConsumptionEntity>

    suspend fun insertRate(rateEntity: RateEntity)
    suspend fun insertRates(rateEntity: List<RateEntity>)
    suspend fun getRates(
        tariffCode: String,
        rateType: RateType,
        validity: ClosedRange<Instant>,
        paymentMethod: PaymentMethod,
    ): List<RateEntity>

    /***
     * Clears all tables in the database
     */
    suspend fun clear()
}
