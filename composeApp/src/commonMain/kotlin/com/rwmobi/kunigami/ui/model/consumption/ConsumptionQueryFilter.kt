/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.consumption

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.endOfMonth
import com.rwmobi.kunigami.domain.extensions.endOfWeek
import com.rwmobi.kunigami.domain.extensions.getDayRange
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.getLocalDayMonthString
import com.rwmobi.kunigami.domain.extensions.getLocalDayOfMonth
import com.rwmobi.kunigami.domain.extensions.getLocalEnglishAbbreviatedDayOfWeekName
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.extensions.getLocalMonthString
import com.rwmobi.kunigami.domain.extensions.getLocalMonthYearString
import com.rwmobi.kunigami.domain.extensions.getLocalYear
import com.rwmobi.kunigami.domain.extensions.getMonthRange
import com.rwmobi.kunigami.domain.extensions.getWeekRange
import com.rwmobi.kunigami.domain.extensions.getYearRange
import com.rwmobi.kunigami.domain.extensions.startOfMonth
import com.rwmobi.kunigami.domain.extensions.startOfWeek
import com.rwmobi.kunigami.domain.extensions.toSystemDefaultLocalDateTime
import com.rwmobi.kunigami.domain.extensions.toSystemDefaultTimeZoneInstant
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import io.github.koalaplot.core.util.toString
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.grouping_label_month_weeks
import kunigami.composeapp.generated.resources.presentation_style_week_seven_days
import kunigami.composeapp.generated.resources.usage_chart_tooltip_range_kwh
import kunigami.composeapp.generated.resources.usage_chart_tooltip_spot_kwh
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration.Companion.nanoseconds

@Immutable
data class ConsumptionQueryFilter(
    val presentationStyle: ConsumptionPresentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
    val referencePoint: Instant = Clock.System.now(),
    val requestedPeriod: ClosedRange<Instant> = calculateQueryPeriod(referencePoint = Clock.System.now(), ConsumptionPresentationStyle.DAY_HALF_HOURLY),
) {
    companion object {
        fun calculateQueryPeriod(referencePoint: Instant, presentationStyle: ConsumptionPresentationStyle): ClosedRange<Instant> {
            val localDateTime = referencePoint.toSystemDefaultLocalDateTime()

            return when (presentationStyle) {
                ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                    referencePoint.getDayRange()
                }

                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                    referencePoint.getWeekRange()
                }

                ConsumptionPresentationStyle.MONTH_WEEKS -> {
                    val startOfWeek = localDateTime.startOfMonth().toSystemDefaultLocalDateTime().startOfWeek()
                    val endOfWeek = localDateTime.endOfMonth().toSystemDefaultLocalDateTime().endOfWeek()
                    startOfWeek..endOfWeek
                }

                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                    referencePoint.getMonthRange()
                }

                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                    referencePoint.getYearRange()
                }
            }
        }
    }

    /***
     * This is for UI display
     */
    fun getConsumptionPeriodString(): String {
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> "${referencePoint.getLocalEnglishAbbreviatedDayOfWeekName()}, ${referencePoint.getLocalDateString()}"
            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> "${requestedPeriod.start.getLocalDateString().substringBefore(delimiter = ",")} - ${requestedPeriod.endInclusive.getLocalDateString()}"
            ConsumptionPresentationStyle.MONTH_WEEKS -> referencePoint.getLocalMonthYearString()
            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> referencePoint.getLocalMonthYearString()
            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> referencePoint.getLocalYear().toString()
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
                        val currentTime = consumption.interval.start.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                        if (currentTime != lastRateValue && currentTime % 2 == 0) {
                            put(
                                key = index,
                                value = currentTime.toString().padStart(length = 2, padChar = '0'),
                            )
                        }
                        lastRateValue = currentTime
                    }
                }

                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(
                            key = index,
                            value = consumption.interval.start.getLocalEnglishAbbreviatedDayOfWeekName(),
                        )
                    }
                }

                ConsumptionPresentationStyle.MONTH_WEEKS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(
                            key = index,
                            value = consumption.interval.start.getLocalDayMonthString(),
                        )
                    }
                }

                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(
                            key = index,
                            value = consumption.interval.start.getLocalDayOfMonth().toString(),
                        )
                    }
                }

                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                    consumptions.forEachIndexed { index, consumption ->
                        put(
                            key = index,
                            value = consumption.interval.start.getLocalMonthString(),
                        )
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
                    .groupBy { it.interval.start.getLocalDateString() }
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
                listOf(
                    ConsumptionGroupedCells(
                        title = getString(resource = Res.string.grouping_label_month_weeks),
                        consumptions = consumptions,
                    ),
                )
            }

            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                consumptions
                    .groupBy { it.interval.start.getLocalMonthYearString() }
                    .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }
            }

            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                consumptions
                    .groupBy { it.interval.start.getLocalYear() }
                    .map { (date, items) -> ConsumptionGroupedCells(title = date.toString(), consumptions = items) }
            }
        }
    }

    suspend fun generateChartToolTips(
        consumptions: List<Consumption>,
    ): List<String> {
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                consumptions.map { consumption ->
                    getString(
                        resource = Res.string.usage_chart_tooltip_range_kwh,
                        consumption.interval.start.getLocalHHMMString(),
                        consumption.interval.start.getLocalHHMMString(),
                        consumption.kWhConsumed.toString(precision = 2),
                    )
                }
            }

            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                consumptions.map { consumption ->
                    getString(
                        resource = Res.string.usage_chart_tooltip_spot_kwh,
                        consumption.interval.start.getLocalDateString(),
                        consumption.kWhConsumed.toString(precision = 2),
                    )
                }
            }

            ConsumptionPresentationStyle.MONTH_WEEKS -> {
                consumptions.map { consumption ->
                    getString(
                        resource = Res.string.usage_chart_tooltip_range_kwh,
                        consumption.interval.start.getLocalDayMonthString(),
                        (consumption.interval.endInclusive - 1.nanoseconds).getLocalDayMonthString(),
                        consumption.kWhConsumed.toString(precision = 2),
                    )
                }
            }

            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                consumptions.map { consumption ->
                    getString(
                        resource = Res.string.usage_chart_tooltip_spot_kwh,
                        consumption.interval.start.getLocalDayMonthString(),
                        consumption.kWhConsumed.toString(precision = 2),
                    )
                }
            }

            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                consumptions.map { consumption ->
                    getString(
                        resource = Res.string.usage_chart_tooltip_spot_kwh,
                        consumption.interval.start.getLocalMonthYearString(),
                        consumption.kWhConsumed.toString(precision = 2),
                    )
                }
            }
        }
    }

    fun canNavigateForward(): Boolean {
        val now = Clock.System.now()
        return getForwardReferencePoint() < now
    }

    /**
     * We assume no smart meter logs before the account move-in date.
     * Although for accurate billing, we should take the tariff start date.
     * We consider the end date for eligibility to make sure we show all available data.
     */
    fun canNavigateBackward(accountMoveInDate: Instant): Boolean {
        val newReferencePoint = getBackwardReferencePoint()
        val newRequestPeriod = calculateQueryPeriod(
            referencePoint = newReferencePoint,
            presentationStyle = presentationStyle,
        )
        return newRequestPeriod.endInclusive >= accountMoveInDate
    }

    /**
     * Generate the ConsumptionQueryFilter for making an API request
     */
    fun navigateBackward(accountMoveInDate: Instant): ConsumptionQueryFilter? {
        if (!canNavigateBackward(accountMoveInDate = accountMoveInDate)) {
            return null
        }

        val newReferencePoint = getBackwardReferencePoint()
        val newRequestPeriod = calculateQueryPeriod(
            referencePoint = newReferencePoint,
            presentationStyle = presentationStyle,
        )

        return ConsumptionQueryFilter(
            presentationStyle = presentationStyle,
            referencePoint = newReferencePoint,
            requestedPeriod = newRequestPeriod,
        )
    }

    /**
     * Generate the ConsumptionQueryFilter for making an API request
     */
    fun navigateForward(): ConsumptionQueryFilter? {
        if (!canNavigateForward()) {
            return null
        }

        val newReferencePoint = getForwardReferencePoint()
        val newRequestPeriod = calculateQueryPeriod(
            referencePoint = newReferencePoint,
            presentationStyle = presentationStyle,
        )

        return ConsumptionQueryFilter(
            presentationStyle = presentationStyle,
            referencePoint = newReferencePoint,
            requestedPeriod = newRequestPeriod,
        )
    }

    /***
     * To do the calculation, we work out from the localised date time regardless of DST,
     * and we only work out the actual Instant after that.
     * It is because 2 months before 1/May 00:00 should always be 1/Mar 00:00 to users, although the GMT representations are not.
     */
    private fun getBackwardReferencePoint(): Instant {
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.minus(value = 1, unit = DateTimeUnit.DAY)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.minus(value = 1, unit = DateTimeUnit.WEEK)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.MONTH_WEEKS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.minus(value = 1, unit = DateTimeUnit.MONTH)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.minus(value = 1, unit = DateTimeUnit.MONTH)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.minus(value = 1, unit = DateTimeUnit.YEAR)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }
        }
    }

    private fun getForwardReferencePoint(): Instant {
        return when (presentationStyle) {
            ConsumptionPresentationStyle.DAY_HALF_HOURLY -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.plus(value = 1, unit = DateTimeUnit.DAY)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.plus(value = 1, unit = DateTimeUnit.WEEK)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.MONTH_WEEKS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.plus(value = 1, unit = DateTimeUnit.MONTH)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.plus(value = 1, unit = DateTimeUnit.MONTH)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }

            ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> with(referencePoint.toSystemDefaultLocalDateTime()) {
                val newDate = date.plus(value = 1, unit = DateTimeUnit.YEAR)
                newDate.atTime(time = time).toSystemDefaultTimeZoneInstant()
            }
        }
    }
}
