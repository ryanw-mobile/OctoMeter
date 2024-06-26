/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object RateSampleData {

    private val timeZone = TimeZone.UTC
    val rateWithValidTo = Rate(
        vatInclusivePrice = 12.0,
        validity = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
        ).toInstant(timeZone)..LocalDateTime(
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
        vatInclusivePrice = 12.0,
        validity = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
        ).toInstant(timeZone)..Instant.DISTANT_FUTURE,
        paymentMethod = PaymentMethod.DIRECT_DEBIT,
    )

    val rates = listOf(
        Rate(
            vatInclusivePrice = 18.291,
            validity = Instant.parse("2024-05-07T21:30:00Z")..Instant.parse("2024-05-07T22:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 18.753,
            validity = Instant.parse("2024-05-07T21:00:00Z")..Instant.parse("2024-05-07T21:30:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 18.291,
            validity = Instant.parse("2024-05-07T20:30:00Z")..Instant.parse("2024-05-07T21:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 20.1915,
            validity = Instant.parse("2024-05-07T20:00:00Z")..Instant.parse("2024-05-07T20:30:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 22.5435,
            validity = Instant.parse("2024-05-07T19:30:00Z")..Instant.parse("2024-05-07T20:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
    )
}
