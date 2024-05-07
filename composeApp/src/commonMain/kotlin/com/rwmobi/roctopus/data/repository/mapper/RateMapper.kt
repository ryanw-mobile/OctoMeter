/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.repository.mapper

import com.rwmobi.roctopus.data.source.network.dto.RateDto
import com.rwmobi.roctopus.domain.model.PaymentMethod
import com.rwmobi.roctopus.domain.model.Rate

fun RateDto.toRate(): Rate {
    return Rate(
        vatExclusivePrice = vatExclusivePrice,
        vatInclusivePrice = vatInclusivePrice,
        validFrom = validFrom,
        validTo = validTo,
        paymentMethod = PaymentMethod.fromValue(paymentMethod),
    )
}

fun List<RateDto>.toRate(): List<Rate> = map { it.toRate() }
