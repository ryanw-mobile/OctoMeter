/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.interfaces

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import kotlinx.datetime.Instant

interface DatabaseDataSource {
    suspend fun insertConsumption(consumptionEntity: ConsumptionEntity)
    suspend fun insertConsumptions(consumptionEntity: List<ConsumptionEntity>)
    suspend fun getConsumptions(meterSerial: String, interval: ClosedRange<Instant>): List<ConsumptionEntity>

    suspend fun insertRate(rateEntity: RateEntity)
    suspend fun insertRates(rateEntity: List<RateEntity>)
    suspend fun getRates(tariffCode: String, rateType: RateType, validity: ClosedRange<Instant>, paymentMethod: String): List<RateEntity>

    /***
     * Clears all tables in the database
     */
    suspend fun clear()
}
