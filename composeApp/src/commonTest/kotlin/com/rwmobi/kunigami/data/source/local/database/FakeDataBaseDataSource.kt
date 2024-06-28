/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.interfaces.DatabaseDataSource
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant

class FakeDataBaseDataSource : DatabaseDataSource {

    var exception: Throwable? = null
    override suspend fun insertConsumption(consumptionEntity: ConsumptionEntity) {
        exception?.let { throw it }
    }

    override suspend fun insertConsumptions(consumptionEntity: List<ConsumptionEntity>) {
        exception?.let { throw it }
    }

    var getConsumptionsResponse: List<ConsumptionEntity>? = null
    override suspend fun getConsumptions(
        meterSerial: String,
        interval: ClosedRange<Instant>,
    ): List<ConsumptionEntity> {
        exception?.let { throw it }
        return getConsumptionsResponse ?: throw RuntimeException("Fake result getConsumptionsResponse not defined")
    }

    override suspend fun insertRate(rateEntity: RateEntity) {
        exception?.let { throw it }
    }

    override suspend fun insertRates(rateEntity: List<RateEntity>) {
        exception?.let { throw it }
    }

    var getRatesResponse: List<RateEntity>? = null
    override suspend fun getRates(
        tariffCode: String,
        rateType: RateType,
        validity: ClosedRange<Instant>,
        paymentMethod: PaymentMethod,
    ): List<RateEntity> {
        exception?.let { throw it }
        return getRatesResponse ?: throw RuntimeException("Fake result getRatesResponse not defined")
    }

    override suspend fun clear() {
        exception?.let { throw it }
    }
}
