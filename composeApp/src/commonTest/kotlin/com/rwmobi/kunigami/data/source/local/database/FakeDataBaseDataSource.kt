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
    override suspend fun insert(consumptionEntity: ConsumptionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(consumptionEntity: List<ConsumptionEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun getConsumptions(meterSerial: String, interval: ClosedRange<Instant>): List<ConsumptionEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }
}
