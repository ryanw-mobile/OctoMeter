/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

fun RateDto.toRate() = Rate(
    vatInclusivePrice = vatInclusivePrice,
    validity = validFrom..(validTo ?: Instant.DISTANT_FUTURE),
    paymentMethod = PaymentMethod.fromValue(paymentMethod),
)
