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

package com.rwmobi.kunigami.data.source.local.database.dao

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlin.time.Instant

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
