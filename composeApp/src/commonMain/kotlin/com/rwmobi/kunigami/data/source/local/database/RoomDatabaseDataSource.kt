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
