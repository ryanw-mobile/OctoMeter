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

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

object RateSampleData {

    private val timeZone = TimeZone.UTC
    val rateWithValidTo = Rate(
        vatInclusivePrice = 12.0,
        validity = LocalDateTime(
            year = 2023,
            month = 5,
            day = 1,
            hour = 0,
            minute = 0,
            second = 0,
        ).toInstant(timeZone)..LocalDateTime(
            year = 2023,
            month = 5,
            day = 10,
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
            month = 5,
            day = 1,
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
