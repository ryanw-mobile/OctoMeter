/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import com.rwmobi.kunigami.data.source.local.database.dao.ConsumptionDao
import com.rwmobi.kunigami.data.source.local.database.dao.RateDao
import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.interfaces.DatabaseDataSource
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant

/***
 * RoomDB automatically switches to IO Dispatcher,
 * This data source acts like a messenger so it is fine relying on the caller's dispatcher
 */
class RoomDatabaseDataSource(
    private val consumptionDao: ConsumptionDao,
    private val rateDao: RateDao,
) : DatabaseDataSource {
    override suspend fun insertConsumption(consumptionEntity: ConsumptionEntity) {
        consumptionDao.insert(consumptionEntity = consumptionEntity)
    }

    override suspend fun insertConsumptions(consumptionEntity: List<ConsumptionEntity>) {
        consumptionDao.insert(consumptionEntity = consumptionEntity)
    }

    override suspend fun insertRate(rateEntity: RateEntity) {
        rateDao.insert(rateEntity = rateEntity)
    }

    override suspend fun insertRates(rateEntity: List<RateEntity>) {
        rateDao.insert(rateEntity = rateEntity)
    }

    override suspend fun getConsumptions(
        meterSerial: String,
        interval: ClosedRange<Instant>,
    ): List<ConsumptionEntity> {
        return consumptionDao.getConsumptions(
            meterSerial = meterSerial,
            intervalStart = interval.start,
            intervalEnd = interval.endInclusive,
        )
    }

    override suspend fun getRates(
        tariffCode: String,
        rateType: RateType,
        validity: ClosedRange<Instant>,
        paymentMethod: PaymentMethod,
    ): List<RateEntity> {
        return rateDao.getRates(
            tariffCode = tariffCode,
            rateType = rateType,
            validFrom = validity.start,
            validTo = if (validity.endInclusive == Instant.DISTANT_FUTURE) null else validity.endInclusive,
            paymentMethod = paymentMethod,
        )
    }

    override suspend fun clear() {
        consumptionDao.clear()
        rateDao.clear()
    }
}
