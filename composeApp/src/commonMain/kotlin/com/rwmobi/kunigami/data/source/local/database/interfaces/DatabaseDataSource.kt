/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.interfaces

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import kotlinx.datetime.Instant

interface DatabaseDataSource {
    suspend fun insert(consumptionEntity: ConsumptionEntity)
    suspend fun insert(consumptionEntity: List<ConsumptionEntity>)
    suspend fun getConsumptions(meterSerial: String, interval: ClosedRange<Instant>): List<ConsumptionEntity>

    /***
     * Clears all tables in the database
     */
    suspend fun clear()
}
