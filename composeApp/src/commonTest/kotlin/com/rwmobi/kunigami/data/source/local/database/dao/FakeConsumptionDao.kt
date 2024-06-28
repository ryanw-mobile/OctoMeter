/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.dao

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import kotlinx.datetime.Instant

class FakeConsumptionDao : ConsumptionDao {
    private val consumptions = mutableListOf<ConsumptionEntity>()

    override suspend fun insert(consumptionEntity: ConsumptionEntity) {
        consumptions.add(consumptionEntity)
    }

    override suspend fun insert(consumptionEntity: List<ConsumptionEntity>) {
        consumptions.addAll(consumptionEntity)
    }

    override suspend fun getConsumptions(meterSerial: String, intervalStart: Instant, intervalEnd: Instant): List<ConsumptionEntity> {
        return consumptions.filter {
            it.meterSerial == meterSerial && it.intervalStart >= intervalStart && it.intervalStart < intervalEnd
        }
    }

    override suspend fun clear() {
        consumptions.clear()
    }
}
