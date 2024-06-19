/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import com.rwmobi.kunigami.data.source.local.database.dao.ConsumptionDao
import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.interfaces.DatabaseDataSource
import kotlinx.datetime.Instant

/***
 * RoomDB automatically switches to IO Dispatcher,
 * This data source acts like a messenger so it is fine relying on the caller's dispatcher
 */
class RoomDatabaseDataSource(
    private val consumptionDao: ConsumptionDao,
) : DatabaseDataSource {
    override suspend fun insert(consumptionEntity: ConsumptionEntity) {
        consumptionDao.insert(consumptionEntity = consumptionEntity)
    }

    override suspend fun insert(consumptionEntity: List<ConsumptionEntity>) {
        consumptionDao.insert(consumptionEntity = consumptionEntity)
    }

    override suspend fun getConsumptions(meterSerial: String, interval: ClosedRange<Instant>): List<ConsumptionEntity> {
        return consumptionDao.getConsumptions(
            meterSerial = meterSerial,
            intervalStart = interval.start,
            intervalEnd = interval.endInclusive,
        )
    }

    override suspend fun clear() {
        consumptionDao.clear()
    }
}
