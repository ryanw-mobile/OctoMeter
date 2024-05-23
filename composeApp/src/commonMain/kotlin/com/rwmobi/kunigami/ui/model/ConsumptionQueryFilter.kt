/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.roundDownToDay
import com.rwmobi.kunigami.domain.extensions.roundUpToDayEnd
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.extensions.toLocalDay
import com.rwmobi.kunigami.domain.extensions.toLocalDayMonth
import com.rwmobi.kunigami.domain.extensions.toLocalMonth
import com.rwmobi.kunigami.domain.extensions.toLocalMonthYear
import com.rwmobi.kunigami.domain.extensions.toLocalWeekday
import com.rwmobi.kunigami.domain.extensions.toLocalYear
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
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
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.presentation_style_week_seven_days
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

@Immutable
data class ConsumptionQueryFilter(
    val presentationStyle: ConsumptionPresentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
    val pointOfReference: Instant = Clock.System.now(),
    val requestedStart: Instant = Clock.System.now().roundDownToDay(),
    val requestedEnd: Instant = Clock.System.now().roundUpToDayEnd(),
) {
    companion object {
        fun calculateStartDate(pointOfReference: Instant, presentationStyle: ConsumptionPresentationStyle): Instant {
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = pointOfReference.toLocalDateTime(timeZone)

            return when (presentationStyle) {
                ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                    pointOfReference.roundDownToDay()
                }

                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                    val dayOfWeek = localDateTime.date.dayOfWeek
                    val daysSinceSunday = dayOfWeek.isoDayNumber
                    val startOfWeek = localDateTime.date
                        .minus(value = daysSinceSunday - 1, unit = DateTimeUnit.DAY)
                        .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                    startOfWeek.toInstant(timeZone)
                }

                ConsumptionPresentationStyle.MONTH_WEEKS -> {
                    val startOfThisMonth = LocalDate(year = localDateTime.year, monthNumber = localDateTime.monthNumber, dayOfMonth = 1)
                        .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                    val dayOfWeek = startOfThisMonth.date.dayOfWeek
                    val daysSinceSunday = dayOfWeek.isoDayNumber
                    val startOfWeek = startOfThisMonth.date
                        .minus(value = daysSinceSunday - 1, unit = DateTimeUnit.DAY)
                        .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                    startOfWeek.toInstant(timeZone)
                }

                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                    val startOfThisMonth = LocalDate(year = localDateTime.year, monthNumber = localDateTime.monthNumber, dayOfMonth = 1)
                        .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                    startOfThisMonth.toInstant(timeZone)
                }

                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                    val startOfThisMonth = LocalDate(year = localDateTime.year, monthNumber = 1, dayOfMonth = 1)
                        .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                    startOfThisMonth.toInstant(timeZone)
                }
            }
        }

        fun calculateEndDate(pointOfReference: Instant, presentationStyle: ConsumptionPresentationStyle): Instant {
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = pointOfReference.toLocalDateTime(timeZone)

            return when (presentationStyle) {
                ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                    pointOfReference.roundUpToDayEnd()
                }

                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                    val dayOfWeek = localDateTime.date.dayOfWeek
                    val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - dayOfWeek.isoDayNumber
                    val endOfWeek = localDateTime.date.plus(daysUntilSunday, DateTimeUnit.DAY)
                        .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999999999)
                    endOfWeek.toInstant(timeZone)
                }

                ConsumptionPresentationStyle.MONTH_WEEKS -> {
                    val startOfNextMonth = LocalDate(year = localDateTime.year, monthNumber = localDateTime.monthNumber, dayOfMonth = 1)
                        .plus(1, DateTimeUnit.MONTH)
                    val endOfMonth = (startOfNextMonth.atStartOfDayIn(timeZone) - 1.nanoseconds).toLocalDateTime(timeZone)
                    val dayOfWeek = endOfMonth.date.dayOfWeek
                    val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - dayOfWeek.isoDayNumber
                    val endOfWeek = endOfMonth.date.plus(daysUntilSunday, DateTimeUnit.DAY)
                        .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999999999)
                    endOfWeek.toInstant(timeZone)
                }

                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                    val startOfNextMonth = LocalDate(year = localDateTime.year, monthNumber = localDateTime.monthNumber, dayOfMonth = 1)
                        .plus(1, DateTimeUnit.MONTH)
                    val endOfMonth = startOfNextMonth.atStartOfDayIn(timeZone) - 1.nanoseconds
                    endOfMonth
                }

                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                    val startOfThisMonth = LocalDate(year = localDateTime.year, monthNumber = 12, dayOfMonth = 31)
                        .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999999999)
                    startOfThisMonth.toInstant(timeZone)
                }
            }
        }
    }

    /***
     * This is for UI display
     */
    fun getConsumptionPeriodString(): String {
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> pointOfReference.toLocalDateString()
            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> "${requestedStart.toLocalDateString().substringBefore(delimiter = ",")} - ${requestedEnd.toLocalDateString()}"
            ConsumptionPresentationStyle.MONTH_WEEKS -> pointOfReference.toLocalMonthYear()
            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> pointOfReference.toLocalMonthYear()
            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> pointOfReference.toLocalYear()
        }
    }

    fun generateChartLabels(
        consumptions: List<Consumption>,
    ): Map<Int, String> {
        return buildMap {
            when (presentationStyle) {
                ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                    var lastRateValue: Int? = null
                    consumptions.forEachIndexed { index, consumption ->
                        val currentTime = consumption.intervalStart.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                        if (currentTime != lastRateValue && currentTime % 2 == 0) {
                            put(key = index, value = currentTime.toString().padStart(2, '0'))
                        }
                        lastRateValue = currentTime
                    }
                }

                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(key = index, value = consumption.intervalStart.toLocalWeekday())
                    }
                }

                ConsumptionPresentationStyle.MONTH_WEEKS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(key = index, value = consumption.intervalStart.toLocalDayMonth())
                    }
                }

                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(key = index, value = consumption.intervalStart.toLocalDay())
                    }
                }

                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(key = index, value = consumption.intervalStart.toLocalMonth())
                    }
                }
            }
        }
    }

    suspend fun groupChartCells(
        consumptions: List<Consumption>,
    ): List<ConsumptionGroupedCells> {
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                consumptions
                    .groupBy { it.intervalStart.toLocalDateString() }
                    .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }
            }

            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                listOf(
                    ConsumptionGroupedCells(
                        title = getString(resource = Res.string.presentation_style_week_seven_days),
                        consumptions = consumptions,
                    ),
                )
            }

            ConsumptionPresentationStyle.MONTH_WEEKS -> {
                consumptions
                    .groupBy { it.intervalStart.toLocalYear() }
                    .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }
            }

            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                consumptions
                    .groupBy { it.intervalStart.toLocalMonthYear() }
                    .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }
            }

            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                consumptions
                    .groupBy { it.intervalStart.toLocalYear() }
                    .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }
            }
        }
    }

    fun canNavigateForward(): Boolean {
        val now = Clock.System.now()
        return getForwardPointOfReference() < now
    }

    /**
     * We assume no smart meter logs before the account move-in date.
     * Although for accurate billing, we should take the tariff start date.
     * We consider the end date for eligibility to make sure we show all available data.
     */
    fun canNavigateBackward(accountMoveInDate: Instant): Boolean {
        val newPointOfReference = getBackwardPointOfReference()
        val newRequestedEnd = calculateEndDate(pointOfReference = newPointOfReference, presentationStyle = presentationStyle)
        return newRequestedEnd >= accountMoveInDate
    }

    /**
     * Generate the ConsumptionQueryFilter for making an API request
     */
    fun navigateBackward(accountMoveInDate: Instant): ConsumptionQueryFilter? {
        if (!canNavigateBackward(accountMoveInDate = accountMoveInDate)) {
            return null
        }

        val newPointOfReference = getBackwardPointOfReference()
        val newRequestedStart = calculateStartDate(pointOfReference = newPointOfReference, presentationStyle = presentationStyle)
        val newRequestedEnd = calculateEndDate(pointOfReference = newPointOfReference, presentationStyle = presentationStyle)

        return ConsumptionQueryFilter(
            presentationStyle = presentationStyle,
            pointOfReference = newPointOfReference,
            requestedStart = newRequestedStart,
            requestedEnd = newRequestedEnd,
        )
    }

    /**
     * Generate the ConsumptionQueryFilter for making an API request
     */
    fun navigateForward(): ConsumptionQueryFilter? {
        if (!canNavigateForward()) {
            return null
        }

        val newPointOfReference = getForwardPointOfReference()
        val newRequestedStart = calculateStartDate(pointOfReference = newPointOfReference, presentationStyle = presentationStyle)
        val newRequestedEnd = calculateEndDate(pointOfReference = newPointOfReference, presentationStyle = presentationStyle)

        return ConsumptionQueryFilter(
            presentationStyle = presentationStyle,
            pointOfReference = newPointOfReference,
            requestedStart = newRequestedStart,
            requestedEnd = newRequestedEnd,
        )
    }

    private fun getBackwardPointOfReference(): Instant {
        val timeZone = TimeZone.currentSystemDefault()
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> pointOfReference.minus(duration = Duration.parseIsoString("P1D"))
            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> pointOfReference.minus(period = DateTimePeriod.parse("P1W"), timeZone = timeZone)
            ConsumptionPresentationStyle.MONTH_WEEKS -> pointOfReference.minus(period = DateTimePeriod.parse("P1M"), timeZone = timeZone)
            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> pointOfReference.minus(period = DateTimePeriod.parse("P1M"), timeZone = timeZone)
            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> pointOfReference.minus(period = DateTimePeriod.parse("P1Y"), timeZone = timeZone)
        }
    }

    private fun getForwardPointOfReference(): Instant {
        val timeZone = TimeZone.currentSystemDefault()
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> pointOfReference.plus(duration = Duration.parseIsoString("P1D"))
            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> pointOfReference.plus(period = DateTimePeriod.parse("P1W"), timeZone = timeZone)
            ConsumptionPresentationStyle.MONTH_WEEKS -> pointOfReference.plus(period = DateTimePeriod.parse("P1M"), timeZone = timeZone)
            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> pointOfReference.plus(period = DateTimePeriod.parse("P1M"), timeZone = timeZone)
            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> pointOfReference.plus(period = DateTimePeriod.parse("P1Y"), timeZone = timeZone)
        }
    }
}
