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
package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.extensions.atEndOfDay
import com.rwmobi.kunigami.domain.extensions.atStartOfDay
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.getLocalEnglishAbbreviatedDayOfWeekName
import com.rwmobi.kunigami.domain.extensions.getLocalMonthYearString
import com.rwmobi.kunigami.domain.extensions.getLocalYear
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Instant

@Suppress("TooManyFunctions")
class ConsumptionQueryFilterTest {

    private val timeZone = TimeZone.of("Europe/London")
    private val now = Clock.System.now()

    @Test
    fun `calculateQueryPeriod should return correct period for DAY_HALF_HOURLY`() {
        val queryPeriod = ConsumptionQueryFilter.calculateQueryPeriod(now, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        assertEquals(now.atStartOfDay(), queryPeriod.start)
        assertEquals(now.atEndOfDay(), queryPeriod.endInclusive)
    }

    @Test
    fun `calculateQueryPeriod should return correct period for WEEK_SEVEN_DAYS`() {
        val expectedStartDate = now
            .toLocalDateTime(timeZone).date
            .minus(now.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)
        val expectedEndDate = now
            .toLocalDateTime(timeZone).date
            .plus(7 - now.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber, DateTimeUnit.DAY)
            .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
            .toInstant(timeZone)

        val queryPeriod = ConsumptionQueryFilter.calculateQueryPeriod(now, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS)

        assertEquals(expectedStartDate, queryPeriod.start)
        assertEquals(expectedEndDate, queryPeriod.endInclusive)
    }

    @Test
    fun `calculateQueryPeriod should return correct period for MONTH_WEEKS`() {
        val localDateTime = now.toLocalDateTime(timeZone)
        val startOfThisMonth = LocalDate(year = localDateTime.year, month = localDateTime.month.number, day = 1)
            .atStartOfDayIn(timeZone)
        val daysSinceSunday = startOfThisMonth
            .toLocalDateTime(timeZone).date
            .dayOfWeek.isoDayNumber
        val expectedStartDate = startOfThisMonth.minus((daysSinceSunday - 1).days)

        val startOfNextMonth = LocalDate(year = localDateTime.year, month = localDateTime.month.number, day = 1)
            .plus(1, DateTimeUnit.MONTH)
            .atStartOfDayIn(timeZone)
        val endOfMonth = (startOfNextMonth - 1.nanoseconds).toLocalDateTime(timeZone)
        val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - endOfMonth.date.dayOfWeek.isoDayNumber
        val expectedEndDate = endOfMonth.date
            .plus(daysUntilSunday, DateTimeUnit.DAY)
            .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
            .toInstant(timeZone)

        val queryPeriod = ConsumptionQueryFilter.calculateQueryPeriod(now, ConsumptionPresentationStyle.MONTH_WEEKS)

        assertEquals(expectedStartDate, queryPeriod.start)
        assertEquals(expectedEndDate, queryPeriod.endInclusive)
    }

    @Test
    fun `calculateQueryPeriod should return correct period for MONTH_THIRTY_DAYS`() {
        val expectedStartDate = LocalDate(now.toLocalDateTime(timeZone).year, now.toLocalDateTime(timeZone).month.number, 1)
            .atStartOfDayIn(timeZone)
        val startOfNextMonth = LocalDate(now.toLocalDateTime(timeZone).year, now.toLocalDateTime(timeZone).month.number, 1)
            .plus(1, DateTimeUnit.MONTH)
            .atStartOfDayIn(timeZone)
        val expectedEndDate = startOfNextMonth - 1.nanoseconds

        val queryPeriod = ConsumptionQueryFilter.calculateQueryPeriod(now, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS)

        assertEquals(expectedStartDate, queryPeriod.start)
        assertEquals(expectedEndDate, queryPeriod.endInclusive)
    }

    @Test
    fun `calculateQueryPeriod should return correct period for YEAR_TWELVE_MONTHS`() {
        val expectedStartDate = LocalDate(now.toLocalDateTime(timeZone).year, 1, 1).atStartOfDayIn(timeZone)
        val expectedEndDate = LocalDate(now.toLocalDateTime(timeZone).year, 12, 31).atTime(23, 59, 59, 999_999_999).toInstant(timeZone)

        val queryPeriod = ConsumptionQueryFilter.calculateQueryPeriod(now, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS)

        assertEquals(expectedStartDate, queryPeriod.start)
        assertEquals(expectedEndDate, queryPeriod.endInclusive)
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for DAY_HALF_HOURLY`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = now)
        assertEquals("${now.getLocalEnglishAbbreviatedDayOfWeekName()}, ${now.getLocalDateString()}", filter.getConsumptionPeriodString())
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for WEEK_SEVEN_DAYS`() {
        val start = now.atStartOfDay()
        val end = now.atEndOfDay()
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, referencePoint = now, requestedPeriod = start..end)
        assertEquals("${start.getLocalDateString().substringBefore(",")} - ${end.getLocalDateString()}", filter.getConsumptionPeriodString())
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for MONTH_WEEKS`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_WEEKS, referencePoint = now)
        assertEquals(now.getLocalMonthYearString(), filter.getConsumptionPeriodString())
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for MONTH_THIRTY_DAYS`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_THIRTY_DAYS, referencePoint = now)
        assertEquals(now.getLocalMonthYearString(), filter.getConsumptionPeriodString())
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for YEAR_TWELVE_MONTHS`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS, referencePoint = now)
        assertEquals(now.getLocalYear().toString(), filter.getConsumptionPeriodString())
    }

    @Test
    fun `canNavigateForward should return false if current time is before forward point of reference`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = now)
        assertFalse(filter.canNavigateForward())
    }

    @Test
    fun `canNavigateBackward should return false if new requested end is before account move-in date`() {
        val accountMoveInDate = Instant.DISTANT_FUTURE
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = now)
        assertFalse(filter.canNavigateBackward(accountMoveInDate))
    }

    @Test
    fun `navigateBackward should return null if cannot navigate backward`() {
        val accountMoveInDate = Instant.DISTANT_FUTURE
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = now)
        assertNull(filter.navigateBackward(accountMoveInDate))
    }

    @Test
    fun `navigateForward should return null if cannot navigate forward`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = now)
        assertNull(filter.navigateForward())
    }

    @Test
    fun `navigateBackward should handle DAY_HALF_HOURLY correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = reference)
        val expected = Instant.parse("2012-03-24T00:00:00Z")

        val newFilter = filter.navigateBackward(firstTariffStartDate = Instant.DISTANT_PAST)

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle WEEK_SEVEN_DAYS correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, referencePoint = reference)
        val expected = Instant.parse("2012-03-18T00:00:00Z")

        val newFilter = filter.navigateBackward(firstTariffStartDate = Instant.DISTANT_PAST)

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle MONTH_WEEKS correctly`() {
        val reference = Instant.parse("2012-03-31T23:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_WEEKS, referencePoint = reference)
        val expected = Instant.parse("2012-03-01T00:00:00Z")

        val newFilter = filter.navigateBackward(firstTariffStartDate = Instant.DISTANT_PAST)

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle MONTH_THIRTY_DAYS correctly`() {
        val reference = Instant.parse("2012-03-31T23:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_THIRTY_DAYS, referencePoint = reference)
        val expected = Instant.parse("2012-03-01T00:00:00Z")

        val newFilter = filter.navigateBackward(firstTariffStartDate = Instant.DISTANT_PAST)

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle YEAR_TWELVE_MONTHS correctly`() {
        val reference = Instant.parse("2012-12-31T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS, referencePoint = reference)
        val expected = Instant.parse("2011-12-31T00:00:00Z")

        val newFilter = filter.navigateBackward(firstTariffStartDate = Instant.DISTANT_PAST)

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle DAY_HALF_HOURLY correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = reference)
        val expected = Instant.parse("2012-03-25T23:00:00Z")

        val newFilter = filter.navigateForward()

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle WEEK_SEVEN_DAYS correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, referencePoint = reference)
        val expected = Instant.parse("2012-03-31T23:00:00Z")

        val newFilter = filter.navigateForward()

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle MONTH_WEEKS correctly`() {
        val reference = Instant.parse("2012-03-01T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_WEEKS, referencePoint = reference)
        val newFilter = filter.navigateForward()

        val expected = Instant.parse("2012-03-31T23:00:00Z")

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle MONTH_THIRTY_DAYS correctly`() {
        val reference = Instant.parse("2012-03-01T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_THIRTY_DAYS, referencePoint = reference)
        val expected = Instant.parse("2012-03-31T23:00:00Z")

        val newFilter = filter.navigateForward()

        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle YEAR_TWELVE_MONTHS correctly`() {
        val reference = Instant.parse("2012-01-01T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS, referencePoint = reference)
        val expected = Instant.parse("2013-01-01T00:00:00Z")

        val newFilter = filter.navigateForward()

        assertEquals(expected, newFilter!!.referencePoint)
    }

    /***
     * Tests for the day of GMT to BST transition
     */
    private val transitionToBST = LocalDate(year = 2012, month = 3, day = 25).atStartOfDayIn(timeZone) // Last Sunday of March 2012
    private val transitionToGMT = LocalDate(year = 2012, month = 10, day = 28).atStartOfDayIn(timeZone) // Last Sunday of October 2012

    @Test
    fun `calculateStartDate should handle GMT to BST transition correctly`() {
        // Since the transition happens at 1 AM UTC (moving clocks forward), the local time of 0:00 is actually 1:00 UTC.
        val transitionInstant = Instant.parse("2012-03-25T00:00:00Z") // Midnight UTC as the start of the day.
        val expectedStartDate = Instant.parse("2012-03-25T00:00:00Z")

        val startDatePeriod = ConsumptionQueryFilter.calculateQueryPeriod(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)

        assertEquals(expectedStartDate, startDatePeriod.start)
    }

    @Test
    fun `calculateEndDate should handle GMT to BST transition correctly`() {
        // The end of the day at midnight on the day clocks go forward, so 23:00 UTC is the last hour of the day.
        val transitionInstant = Instant.parse("2012-03-25T00:00:00Z")
        val expectedEndDate = Instant.parse("2012-03-25T22:59:59.999999999Z") // 23:00 UTC because we lose an hour (the day is only 23 hours long).

        val endDatePeriod = ConsumptionQueryFilter.calculateQueryPeriod(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)

        assertEquals(expectedEndDate, endDatePeriod.endInclusive)
    }

    @Test
    fun `calculateStartDate should handle BST to GMT transition correctly`() {
        // The day starts at midnight, unaffected by the transition that will occur at 2 AM local (which is 1 AM UTC).
        val transitionInstant = Instant.parse("2012-10-28T00:00:00Z")
        val expectedStartDate = Instant.parse("2012-10-27T23:00:00Z")

        val startDatePeriod = ConsumptionQueryFilter.calculateQueryPeriod(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)

        assertEquals(expectedStartDate, startDatePeriod.start)
    }

    @Test
    fun `calculateEndDate should handle BST to GMT transition correctly`() {
        // The day ends at midnight following a 25-hour long day because clocks are set back one hour.
        val transitionInstant = Instant.parse("2012-10-28T00:00:00Z")
        val expectedEndDate = Instant.parse("2012-10-28T23:59:59.999999999Z") // It is still 23:59:59.999999999 UTC, reflecting 25 hours total.

        val endDatePeriod = ConsumptionQueryFilter.calculateQueryPeriod(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)

        assertEquals(expectedEndDate, endDatePeriod.endInclusive)
    }

    @Test
    fun `navigateBackward should correctly handle the day of GMT to BST transition`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = transitionToBST)
        // Expected timestamp for one day before the BST transition
        val expectedTimestamp = LocalDate(year = 2012, month = 3, day = 24).atStartOfDayIn(timeZone)

        val newFilter = filter.navigateBackward(Instant.DISTANT_PAST) // Assume account move-in date allows backward navigation

        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should correctly handle the day of BST to GMT transition`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = transitionToGMT)
        // Expected timestamp for one day after the GMT transition
        val expectedTimestamp = LocalDate(year = 2012, month = 10, day = 29).atStartOfDayIn(timeZone)

        val newFilter = filter.navigateForward() // Assuming no constraints on moving forward

        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should correctly handle the day before transitioning from BST to GMT`() {
        val dayBeforeTransition = LocalDate(year = 2012, month = 10, day = 27).atStartOfDayIn(timeZone)
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = dayBeforeTransition)
        // Expected timestamp for two days before the GMT transition
        val expectedTimestamp = LocalDate(year = 2012, month = 10, day = 26).atStartOfDayIn(timeZone)

        val newFilter = filter.navigateBackward(Instant.DISTANT_PAST)

        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should correctly handle the day before transitioning from BST to GMT`() {
        val dayBeforeTransition = LocalDate(year = 2012, month = 10, day = 27).atStartOfDayIn(timeZone)
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = dayBeforeTransition)
        // Forward to the transition day
        val expectedTimestamp = LocalDate(year = 2012, month = 10, day = 28).atStartOfDayIn(timeZone)

        val newFilter = filter.navigateForward()

        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }
}
