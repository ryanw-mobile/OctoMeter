/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.extensions.roundToDayEnd
import com.rwmobi.kunigami.domain.extensions.roundToDayStart
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.extensions.toLocalMonthYear
import com.rwmobi.kunigami.domain.extensions.toLocalWeekday
import com.rwmobi.kunigami.domain.extensions.toLocalYear
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.nanoseconds

class ConsumptionQueryFilterTest {

    private val timeZone = TimeZone.currentSystemDefault()
    private val now = Clock.System.now()

    @Test
    fun `calculateStartDate should return correct start date for DAY_HALF_HOURLY`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        startDate shouldBe now.roundToDayStart()
    }

    @Test
    fun `calculateEndDate should return correct end date for DAY_HALF_HOURLY`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.DAY_HALF_HOURLY)
        endDate shouldBe now.roundToDayEnd()
    }

    @Test
    fun `calculateStartDate should return correct start date for WEEK_SEVEN_DAYS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS)
        val expectedStartDate = now.toLocalDateTime(timeZone).date.minus(now.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
        startDate shouldBe expectedStartDate
    }

    @Test
    fun `calculateEndDate should return correct end date for WEEK_SEVEN_DAYS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS)
        val expectedEndDate = now.toLocalDateTime(timeZone).date.plus(7 - now.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber, DateTimeUnit.DAY).atTime(23, 59, 59, 999999999).toInstant(timeZone)
        endDate shouldBe expectedEndDate
    }

    @Test
    fun `calculateStartDate should return correct start date for MONTH_WEEKS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.MONTH_WEEKS)
        val localDateTime = now.toLocalDateTime(timeZone)
        val startOfThisMonth = LocalDate(localDateTime.year, localDateTime.monthNumber, 1).atStartOfDayIn(timeZone)
        val daysSinceSunday = startOfThisMonth.toLocalDateTime(timeZone).date.dayOfWeek.isoDayNumber
        val expectedStartDate = startOfThisMonth.minus((daysSinceSunday - 1).days)
        startDate shouldBe expectedStartDate
    }

    @Test
    fun `calculateEndDate should return correct end date for MONTH_WEEKS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.MONTH_WEEKS)
        val localDateTime = now.toLocalDateTime(timeZone)
        val startOfNextMonth = LocalDate(localDateTime.year, localDateTime.monthNumber, 1).plus(1, DateTimeUnit.MONTH).atStartOfDayIn(timeZone)
        val endOfMonth = (startOfNextMonth - 1.nanoseconds).toLocalDateTime(timeZone)
        val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - endOfMonth.date.dayOfWeek.isoDayNumber
        val expectedEndDate = endOfMonth.date.plus(daysUntilSunday, DateTimeUnit.DAY).atTime(23, 59, 59, 999999999).toInstant(timeZone)
        endDate shouldBe expectedEndDate
    }

    @Test
    fun `calculateStartDate should return correct start date for MONTH_THIRTY_DAYS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS)
        val expectedStartDate = LocalDate(now.toLocalDateTime(timeZone).year, now.toLocalDateTime(timeZone).monthNumber, 1).atStartOfDayIn(timeZone)
        startDate shouldBe expectedStartDate
    }

    @Test
    fun `calculateEndDate should return correct end date for MONTH_THIRTY_DAYS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS)
        val startOfNextMonth = LocalDate(now.toLocalDateTime(timeZone).year, now.toLocalDateTime(timeZone).monthNumber, 1).plus(1, DateTimeUnit.MONTH).atStartOfDayIn(timeZone)
        val expectedEndDate = startOfNextMonth - 1.nanoseconds
        endDate shouldBe expectedEndDate
    }

    @Test
    fun `calculateStartDate should return correct start date for YEAR_TWELVE_MONTHS`() {
        val startDate = ConsumptionQueryFilter.calculateStartDate(now, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS)
        val expectedStartDate = LocalDate(now.toLocalDateTime(timeZone).year, 1, 1).atStartOfDayIn(timeZone)
        startDate shouldBe expectedStartDate
    }

    @Test
    fun `calculateEndDate should return correct end date for YEAR_TWELVE_MONTHS`() {
        val endDate = ConsumptionQueryFilter.calculateEndDate(now, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS)
        val expectedEndDate = LocalDate(now.toLocalDateTime(timeZone).year, 12, 31).atTime(23, 59, 59, 999999999).toInstant(timeZone)
        endDate shouldBe expectedEndDate
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for DAY_HALF_HOURLY`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = now)
        filter.getConsumptionPeriodString() shouldBe "${now.toLocalWeekday()}, ${now.toLocalDateString()}"
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for WEEK_SEVEN_DAYS`() {
        val start = now.roundToDayStart()
        val end = now.roundToDayEnd()
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS, pointOfReference = now, requestedStart = start, requestedEnd = end)
        filter.getConsumptionPeriodString() shouldBe "${start.toLocalDateString().substringBefore(",")} - ${end.toLocalDateString()}"
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for MONTH_WEEKS`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_WEEKS, pointOfReference = now)
        filter.getConsumptionPeriodString() shouldBe now.toLocalMonthYear()
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for MONTH_THIRTY_DAYS`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.MONTH_THIRTY_DAYS, pointOfReference = now)
        filter.getConsumptionPeriodString() shouldBe now.toLocalMonthYear()
    }

    @Test
    fun `getConsumptionPeriodString should return correct string for YEAR_TWELVE_MONTHS`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS, pointOfReference = now)
        filter.getConsumptionPeriodString() shouldBe now.toLocalYear()
    }

    @Test
    fun `canNavigateForward should return false if current time is before forward point of reference`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = now)
        filter.canNavigateForward() shouldBe false
    }

    @Test
    fun `canNavigateBackward should return false if new requested end is before account move-in date`() {
        val accountMoveInDate = Instant.DISTANT_FUTURE
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = now)
        filter.canNavigateBackward(accountMoveInDate) shouldBe false
    }

    @Test
    fun `navigateBackward should return null if cannot navigate backward`() {
        val accountMoveInDate = Instant.DISTANT_FUTURE
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = now)
        filter.navigateBackward(accountMoveInDate) shouldBe null
    }

    @Test
    fun `navigateForward should return null if cannot navigate forward`() {
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = now)
        filter.navigateForward() shouldBe null
    }

    @Test
    fun `navigateBackward should return new filter if can navigate backward`() {
        val accountMoveInDate = Instant.DISTANT_PAST
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = now)
        val newFilter = filter.navigateBackward(accountMoveInDate)
        newFilter shouldNotBe null
        newFilter!!.pointOfReference shouldBe now - Duration.parse("1d")
    }

    @Test
    fun `navigateForward should return new filter if can navigate forward`() {
        val futureTime = now - Duration.parse("1d")
        val filter = ConsumptionQueryFilter(presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY, pointOfReference = futureTime)
        val newFilter = filter.navigateForward()
        newFilter shouldNotBe null
        newFilter!!.pointOfReference shouldBe now
    }
}
