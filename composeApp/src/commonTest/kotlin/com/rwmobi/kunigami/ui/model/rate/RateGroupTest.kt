package com.rwmobi.kunigami.ui.model.rate

import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration

class RateGroupTest {

    private val londonZone = TimeZone.of("Europe/London")
    private val testGroupTitle = "Test Group"

    @Test
    fun `findActiveRate should return active rate when it exists`() {
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
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate),
            ),
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 5,
            hour = 0,
            minute = 0,
        ).toInstant(londonZone)

        val activeRate = partitionedList.findActiveRate(referencePoint)

        assertNotNull(activeRate)
        assertEquals(activeRate, rate)
    }

    @Test
    fun `findActiveRate should return null when no active rate exists`() {
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
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate),
            ),
        )
        val referencePoint = LocalDateTime(
            year = 2023,
            monthNumber = 5,
            dayOfMonth = 11,
            hour = 0,
            minute = 0,

        ).toInstant(londonZone)

        val activeRate = partitionedList.findActiveRate(referencePoint)

        assertNull(activeRate)
    }

    @Test
    fun `getRateTrend should return STEADY when active rate has no validTo`() {
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
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate),
            ),
        )

        val trend = partitionedList.getRateTrend(rate)

        assertEquals(trend, RateTrend.STEADY)
    }

    @Test
    fun `getRateTrend should return null when no next rate exists`() {
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
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate),
            ),
        )

        val trend = partitionedList.getRateTrend(rate)

        assertNull(trend)
    }

    @Test
    fun `getRateTrend should return UP when next rate is higher`() {
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
        val nextRate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 15.0,
            validity = validTo.plus(Duration.parse("PT1S"))..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate, nextRate),
            ),
        )

        val trend = partitionedList.getRateTrend(rate)

        assertEquals(trend, RateTrend.UP)
    }

    @Test
    fun `getRateTrend should return DOWN when next rate is lower`() {
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
        val nextRate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 10.0,
            validity = validTo.plus(Duration.parse("PT1S"))..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate, nextRate),
            ),
        )

        val trend = partitionedList.getRateTrend(rate)

        assertEquals(trend, RateTrend.DOWN)
    }

    @Test
    fun `getRateTrend should return STEADY when next rate is the same`() {
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
        val nextRate = Rate(
            vatExclusivePrice = 10.0,
            vatInclusivePrice = 12.0,
            validity = validTo.plus(Duration.parse("PT1S"))..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val partitionedList = listOf(
            RateGroup(
                title = testGroupTitle,
                rates = listOf(rate, nextRate),
            ),
        )

        val trend = partitionedList.getRateTrend(rate)

        assertEquals(trend, RateTrend.STEADY)
    }
}
