/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

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
