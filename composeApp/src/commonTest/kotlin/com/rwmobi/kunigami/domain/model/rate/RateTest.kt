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

package com.rwmobi.kunigami.domain.model.rate

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

class RateTest {

    private val londonZone = TimeZone.of("Europe/London")

    @Test
    fun `isActive should return true when point of reference is within the valid period`() {
        val validFrom = LocalDateTime(
            year = 2023,
            month = 5,
            day = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val validTo = LocalDateTime(
            year = 2023,
            month = 5,
            day = 10,
            hour = 23,
            minute = 59,
        ).toInstant(londonZone)
        val rate = Rate(
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            month = 5,
            day = 5,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        assertTrue(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return false when point of reference is before the valid period`() {
        val validFrom = LocalDateTime(
            year = 2023,
            month = 5,
            day = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val validTo = LocalDateTime(
            year = 2023,
            month = 5,
            day = 10,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)
        val rate = Rate(
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            month = 4,
            day = 30,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)

        assertFalse(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return false when point of reference is after the valid period`() {
        val validFrom = LocalDateTime(
            year = 2023,
            month = 5,
            day = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val validTo = LocalDateTime(
            year = 2023,
            month = 5,
            day = 10,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)
        val rate = Rate(
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            month = 5,
            day = 11,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        assertFalse(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return true when validTo is null and point of reference is after validFrom`() {
        val validFrom = LocalDateTime(
            year = 2023,
            month = 5,
            day = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val rate = Rate(
            vatInclusivePrice = 12.0,
            validity = validFrom..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            month = 5,
            day = 5,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        assertTrue(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return false when validTo is null and point of reference is before validFrom`() {
        val validFrom = LocalDateTime(
            year = 2023,
            month = 5,
            day = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val rate = Rate(
            vatInclusivePrice = 12.0,
            validity = validFrom..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            month = 4,
            day = 30,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)

        assertFalse(rate.isActive(referencePoint))
    }
}
