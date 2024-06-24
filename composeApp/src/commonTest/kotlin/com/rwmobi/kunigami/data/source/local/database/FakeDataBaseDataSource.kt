/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.interfaces.DatabaseDataSource
import kotlinx.datetime.Instant

class FakeDataBaseDataSource : DatabaseDataSource {

    var exception: Throwable? = null
    override suspend fun insert(consumptionEntity: ConsumptionEntity) {
        exception?.let { throw it }
    }

    override suspend fun insert(consumptionEntity: List<ConsumptionEntity>) {
        exception?.let { throw it }
    }

    var getConsumptionsResponse: List<ConsumptionEntity>? = null
    override suspend fun getConsumptions(meterSerial: String, interval: ClosedRange<Instant>): List<ConsumptionEntity> {
        exception?.let { throw it }
        return getConsumptionsResponse ?: throw RuntimeException("Fake result getConsumptionsResponse not defined")
    }

    override suspend fun clear() {
        exception?.let { throw it }
    }
}
