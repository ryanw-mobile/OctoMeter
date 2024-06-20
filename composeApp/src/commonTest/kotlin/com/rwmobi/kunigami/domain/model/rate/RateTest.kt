package com.rwmobi.kunigami.domain.model.rate

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RateTest {

    private val londonZone = TimeZone.of("Europe/London")

    @Test
    fun `isActive should return true when point of reference is within the valid period`() {
        val validFrom = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val validTo = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 10,
            hour = 23,
            minute = 59,
        ).toInstant(londonZone)
        val rate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 5,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        assertTrue(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return false when point of reference is before the valid period`() {
        val validFrom = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val validTo = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 10,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)
        val rate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 4,
            dayOfMonth = 30,
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
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val validTo = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 10,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)
        val rate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 11,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        assertFalse(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return true when validTo is null and point of reference is after validFrom`() {
        val validFrom = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val rate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 12.0,
            validity = validFrom..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 5,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        assertTrue(rate.isActive(referencePoint))
    }

    @Test
    fun `isActive should return false when validTo is null and point of reference is before validFrom`() {
        val validFrom = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)
        val rate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 12.0,
            validity = validFrom..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 4,
            dayOfMonth = 30,
            hour = 23,
            minute = 59,
            second = 59,
        ).toInstant(londonZone)

        assertFalse(rate.isActive(referencePoint))
    }
}
