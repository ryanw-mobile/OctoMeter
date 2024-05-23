/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.samples

import com.rwmobi.kunigami.domain.model.PaymentMethod
import com.rwmobi.kunigami.domain.model.Rate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object RateSampleData {

    private val timeZone = TimeZone.UTC
    val rateWithValidTo = Rate(
        vatExclusivePrice = 10.0,
        vatInclusivePrice = 12.0,
        validFrom = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
        ).toInstant(timeZone),
        validTo = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 10,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(timeZone),
        paymentMethod = PaymentMethod.DIRECT_DEBIT,
    )

    val rateWithoutValidTo = Rate(
        vatExclusivePrice = 10.0,
        vatInclusivePrice = 12.0,
        validFrom = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
        ).toInstant(timeZone),
        validTo = null,
        paymentMethod = PaymentMethod.DIRECT_DEBIT,
    )
}
