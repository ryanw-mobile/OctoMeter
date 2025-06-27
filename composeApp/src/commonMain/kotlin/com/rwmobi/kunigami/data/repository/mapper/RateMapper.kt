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

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlin.time.Instant

fun RateDto.toRate() = Rate(
    vatInclusivePrice = vatInclusivePrice,
    validity = validFrom..(validTo ?: Instant.DISTANT_FUTURE),
    paymentMethod = PaymentMethod.fromValue(paymentMethod),
)

fun RateDto.toRateEntity(
    tariffCode: String,
    rateType: RateType,
) = RateEntity(
    tariffCode = tariffCode,
    rateType = rateType,
    paymentMethod = PaymentMethod.fromValue(value = paymentMethod),
    validFrom = validFrom,
    validTo = if (validTo == Instant.DISTANT_FUTURE) null else validTo,
    vatRate = vatInclusivePrice,
)

fun RateEntity.toRate() = Rate(
    vatInclusivePrice = vatRate,
    validity = validFrom..(validTo ?: Instant.DISTANT_FUTURE),
    paymentMethod = paymentMethod,
)
