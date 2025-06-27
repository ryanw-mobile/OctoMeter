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

package com.rwmobi.kunigami.ui.model.rate

import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration
import kotlin.time.Instant

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
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val nextRate = Rate(
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
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val nextRate = Rate(
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
            vatInclusivePrice = 12.0,
            validity = validFrom..validTo,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        )
        val nextRate = Rate(
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
