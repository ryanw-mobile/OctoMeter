/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.extensions.atEndOfDay
import com.rwmobi.kunigami.domain.extensions.atStartOfDay
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.getLocalEnglishAbbreviatedDayOfWeekName
import com.rwmobi.kunigami.domain.extensions.getLocalMonthYearString
import com.rwmobi.kunigami.domain.extensions.getLocalYear
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.nanoseconds

class ConsumptionQueryFilterTest {

    private val timeZone = TimeZone.of("Europe/London")
    private val now = Clock.System.now()

    @Test
    fun `calculateStartDate should return correct start date for DAY_HALF_HOURLY`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        assertEquals(now.atStartOfDay(), startDate)
    }

    @Test
    fun `calculateEndDate should return correct end date for DAY_HALF_HOURLY`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        assertEquals(now.atEndOfDay(), endDate)
    }

    @Test
    fun `calculateStartDate should return correct start date for WEEK_SEVEN_DAYS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS)
        val expectedStartDate = now.toLocalDateTime(timeZone).date.minus(now.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
        assertEquals(expectedStartDate, startDate)
    }

    @Test
    fun `calculateEndDate should return correct end date for WEEK_SEVEN_DAYS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS)
        val expectedEndDate = now.toLocalDateTime(timeZone).date.plus(7 - now.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber, DateTimeUnit.DAY).atTime(23, 59, 59, 999_999_999).toInstant(timeZone)
        assertEquals(expectedEndDate, endDate)
    }

    @Test
    fun `calculateStartDate should return correct start date for MONTH_WEEKS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.MONTH_WEEKS)
        val localDateTime = now.toLocalDateTime(timeZone)
        val startOfThisMonth = LocalDate(localDateTime.year, localDateTime.monthNumber, 1).atStartOfDayIn(timeZone)
        val daysSinceSunday = startOfThisMonth.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber
        val expectedStartDate = startOfThisMonth.minus((daysSinceSunday - 1).days)
        assertEquals(expectedStartDate, startDate)
    }

    @Test
    fun `calculateEndDate should return correct end date for MONTH_WEEKS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.MONTH_WEEKS)
        val localDateTime = now.toLocalDateTime(timeZone)
        val startOfNextMonth = LocalDate(localDateTime.year, localDateTime.monthNumber, 1).plus(1, DateTimeUnit.MONTH).atStartOfDayIn(timeZone)
        val endOfMonth = (startOfNextMonth - 1.nanoseconds).toLocalDateTime(timeZone)
        val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - endOfMonth.date.dayOfWeek.isoDayNumber
        val expectedEndDate = endOfMonth.date.plus(daysUntilSunday, DateTimeUnit.DAY).atTime(23, 59, 59, 999_999_999).toInstant(timeZone)
        assertEquals(expectedEndDate, endDate)
    }

    @Test
    fun `calculateStartDate should return correct start date for MONTH_THIRTY_DAYS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS)
        val expectedStartDate = LocalDate(now.toLocalDateTime(timeZone).year, now.toLocalDateTime(timeZone).monthNumber, 1).atStartOfDayIn(timeZone)
        assertEquals(expectedStartDate, startDate)
    }

    @Test
    fun `calculateEndDate should return correct end date for MONTH_THIRTY_DAYS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS)
        val startOfNextMonth = LocalDate(now.toLocalDateTime(timeZone).year, now.toLocalDateTime(timeZone).monthNumber, 1).plus(1, DateTimeUnit.MONTH).atStartOfDayIn(timeZone)
        val expectedEndDate = startOfNextMonth - 1.nanoseconds
        assertEquals(expectedEndDate, endDate)
    }

    @Test
    fun `calculateStartDate should return correct start date for YEAR_TWELVE_MONTHS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS)
        val expectedStartDate = LocalDate(now.toLocalDateTime(timeZone).year, 1, 1).atStartOfDayIn(timeZone)
        assertEquals(expectedStartDate, startDate)
    }

    @Test
    fun `calculateEndDate should return correct end date for YEAR_TWELVE_MONTHS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS)
        val expectedEndDate = LocalDate(now.toLocalDateTime(timeZone).year, 12, 31).atTime(23, 59, 59, 999_999_999).toInstant(timeZone)
        assertEquals(expectedEndDate, endDate)
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
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, referencePoint = now, requestedStart = start, requestedEnd = end)
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
        val newFilter = filter.navigateBackward(accountMoveInDate = Instant.DISTANT_PAST)
        val expected = Instant.parse("2012-03-24T00:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle WEEK_SEVEN_DAYS correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, referencePoint = reference)
        val newFilter = filter.navigateBackward(accountMoveInDate = Instant.DISTANT_PAST)
        val expected = Instant.parse("2012-03-18T00:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle MONTH_WEEKS correctly`() {
        val reference = Instant.parse("2012-03-31T23:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_WEEKS, referencePoint = reference)
        val newFilter = filter.navigateBackward(accountMoveInDate = Instant.DISTANT_PAST)
        val expected = Instant.parse("2012-03-01T00:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle MONTH_THIRTY_DAYS correctly`() {
        val reference = Instant.parse("2012-03-31T23:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_THIRTY_DAYS, referencePoint = reference)
        val newFilter = filter.navigateBackward(accountMoveInDate = Instant.DISTANT_PAST)
        val expected = Instant.parse("2012-03-01T00:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should handle YEAR_TWELVE_MONTHS correctly`() {
        val reference = Instant.parse("2012-12-31T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS, referencePoint = reference)
        val newFilter = filter.navigateBackward(accountMoveInDate = Instant.DISTANT_PAST)
        val expected = Instant.parse("2011-12-31T00:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle DAY_HALF_HOURLY correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = reference)
        val newFilter = filter.navigateForward()
        val expected = Instant.parse("2012-03-25T23:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle WEEK_SEVEN_DAYS correctly`() {
        val reference = Instant.parse("2012-03-25T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, referencePoint = reference)
        val newFilter = filter.navigateForward()
        val expected = Instant.parse("2012-03-31T23:00:00Z")
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
        val newFilter = filter.navigateForward()
        val expected = Instant.parse("2012-03-31T23:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should handle YEAR_TWELVE_MONTHS correctly`() {
        val reference = Instant.parse("2012-01-01T00:00:00Z")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS, referencePoint = reference)
        val newFilter = filter.navigateForward()
        val expected = Instant.parse("2013-01-01T00:00:00Z")
        assertEquals(expected, newFilter!!.referencePoint)
    }

    /***
     * Tests for the day of GMT to BST transition
     */
    private val transitionToBST = LocalDate(year = 2012, monthNumber = 3, dayOfMonth = 25).atStartOfDayIn(timeZone) // Last Sunday of March 2012
    private val transitionToGMT = LocalDate(year = 2012, monthNumber = 10, dayOfMonth = 28).atStartOfDayIn(timeZone) // Last Sunday of October 2012

    @Test
    fun `calculateStartDate should handle GMT to BST transition correctly`() {
        // Since the transition happens at 1 AM UTC (moving clocks forward), the local time of 0:00 is actually 1:00 UTC.
        val transitionInstant = Instant.parse("2012-03-25T00:00:00Z") // Midnight UTC as the start of the day.
        val startDate = ConsumptionQueryFilter.calculateStartDate(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        val expectedStartDate = Instant.parse("2012-03-25T00:00:00Z")
        assertEquals(expectedStartDate, startDate)
    }

    @Test
    fun `calculateEndDate should handle GMT to BST transition correctly`() {
        // The end of the day at midnight on the day clocks go forward, so 23:00 UTC is the last hour of the day.
        val transitionInstant = Instant.parse("2012-03-25T00:00:00Z")
        val endDate = ConsumptionQueryFilter.calculateEndDate(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        val expectedEndDate = Instant.parse("2012-03-25T22:59:59.999999999Z") // 23:00 UTC because we lose an hour (the day is only 23 hours long).
        assertEquals(expectedEndDate, endDate)
    }

    @Test
    fun `calculateStartDate should handle BST to GMT transition correctly`() {
        // The day starts at midnight, unaffected by the transition that will occur at 2 AM local (which is 1 AM UTC).
        val transitionInstant = Instant.parse("2012-10-28T00:00:00Z")
        val startDate = ConsumptionQueryFilter.calculateStartDate(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        val expectedStartDate = Instant.parse("2012-10-27T23:00:00Z")
        assertEquals(expectedStartDate, startDate)
    }

    @Test
    fun `calculateEndDate should handle BST to GMT transition correctly`() {
        // The day ends at midnight following a 25-hour long day because clocks are set back one hour.
        val transitionInstant = Instant.parse("2012-10-28T00:00:00Z")
        val endDate = ConsumptionQueryFilter.calculateEndDate(transitionInstant, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        val expectedEndDate = Instant.parse("2012-10-28T23:59:59.999999999Z") // It is still 23:59:59.999999999 UTC, reflecting 25 hours total.
        assertEquals(expectedEndDate, endDate)
    }

    @Test
    fun `navigateBackward should correctly handle the day of GMT to BST transition`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = transitionToBST)
        val newFilter = filter.navigateBackward(Instant.DISTANT_PAST) // Assume account move-in date allows backward navigation
        // Expected timestamp for one day before the BST transition
        val expectedTimestamp = LocalDate(year = 2012, monthNumber = 3, dayOfMonth = 24).atStartOfDayIn(timeZone)
        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should correctly handle the day of BST to GMT transition`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = transitionToGMT)
        val newFilter = filter.navigateForward() // Assuming no constraints on moving forward
        // Expected timestamp for one day after the GMT transition
        val expectedTimestamp = LocalDate(year = 2012, monthNumber = 10, dayOfMonth = 29).atStartOfDayIn(timeZone)
        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateBackward should correctly handle the day before transitioning from BST to GMT`() {
        val dayBeforeTransition = LocalDate(year = 2012, monthNumber = 10, dayOfMonth = 27).atStartOfDayIn(timeZone)
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = dayBeforeTransition)
        val newFilter = filter.navigateBackward(Instant.DISTANT_PAST)
        // Expected timestamp for two days before the GMT transition
        val expectedTimestamp = LocalDate(year = 2012, monthNumber = 10, dayOfMonth = 26).atStartOfDayIn(timeZone)
        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }

    @Test
    fun `navigateForward should correctly handle the day before transitioning from BST to GMT`() {
        val dayBeforeTransition = LocalDate(year = 2012, monthNumber = 10, dayOfMonth = 27).atStartOfDayIn(timeZone)
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, referencePoint = dayBeforeTransition)
        val newFilter = filter.navigateForward()
        // Forward to the transition day
        val expectedTimestamp = LocalDate(year = 2012, monthNumber = 10, dayOfMonth = 28).atStartOfDayIn(timeZone)
        assertEquals(expectedTimestamp, newFilter!!.referencePoint)
    }
}
