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

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant

class FakeRateDao : RateDao {
    private val rates = mutableListOf<RateEntity>()

    override suspend fun insert(rateEntity: RateEntity) {
        rates.add(rateEntity)
    }

    override suspend fun insert(rateEntity: List<RateEntity>) {
        rates.addAll(rateEntity)
    }

    override suspend fun getRates(
        tariffCode: String,
        rateType: RateType,
        paymentMethod: PaymentMethod,
        validFrom: Instant,
        validTo: Instant?,
    ): List<RateEntity> {
        return rates.filter {
            it.tariffCode == tariffCode &&
                it.rateType == rateType &&
                it.paymentMethod == paymentMethod &&
                it.validFrom >= validFrom &&
                (validTo == null || it.validFrom < validTo)
        }
    }

    override suspend fun clear() {
        rates.clear()
    }
}
