/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.RateDto
import com.rwmobi.kunigami.domain.model.PaymentMethod
import com.rwmobi.kunigami.domain.model.Rate

fun RateDto.toRate() = Rate(
    vatExclusivePrice = vatExclusivePrice,
    vatInclusivePrice = vatInclusivePrice,
    validFrom = validFrom,
    validTo = validTo,
    paymentMethod = PaymentMethod.fromValue(paymentMethod),
)
