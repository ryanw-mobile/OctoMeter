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

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.endOfMonth
import com.rwmobi.kunigami.domain.extensions.endOfWeek
import com.rwmobi.kunigami.domain.extensions.getDayRange
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.getLocalEnglishAbbreviatedDayOfWeekName
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
import com.rwmobi.kunigami.ui.tools.interfaces.StringResourceProvider
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.grouping_label_month_weeks
import kunigami.composeapp.generated.resources.presentation_style_week_seven_days
import kotlin.time.Clock
import kotlin.time.Instant

@Immutable
data class ConsumptionQueryFilter(
    val presentationStyle: ConsumptionPresentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
    val referencePoint: Instant = Clock.System.now(),
    val requestedPeriod: ClosedRange<Instant> = calculateQueryPeriod(referencePoint = referencePoint, ConsumptionPresentationStyle.DAY_HALF_HOURLY),
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
    fun getConsumptionPeriodString(): String = when (presentationStyle) {
        ConsumptionPresentationStyle.DAY_HALF_HOURLY -> "${referencePoint.getLocalEnglishAbbreviatedDayOfWeekName()}, ${referencePoint.getLocalDateString()}"
        ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> "${requestedPeriod.start.getLocalDateString().substringBefore(delimiter = ",")} - ${requestedPeriod.endInclusive.getLocalDateString()}"
        ConsumptionPresentationStyle.MONTH_WEEKS -> referencePoint.getLocalMonthYearString()
        ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> referencePoint.getLocalMonthYearString()
        ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> referencePoint.getLocalYear().toString()
    }

    suspend fun groupChartCells(
        consumptions: List<Consumption>,
        stringResourceProvider: StringResourceProvider,
    ): List<ConsumptionGroupedCells> = when (presentationStyle) {
        ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
            consumptions
                .groupBy { it.interval.start.getLocalDateString() }
                .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }
        }

        ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
            listOf(
                ConsumptionGroupedCells(
                    title = stringResourceProvider.getString(resource = Res.string.presentation_style_week_seven_days),
                    consumptions = consumptions,
                ),
            )
        }

        ConsumptionPresentationStyle.MONTH_WEEKS -> {
            listOf(
                ConsumptionGroupedCells(
                    title = stringResourceProvider.getString(resource = Res.string.grouping_label_month_weeks),
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

    fun canNavigateForward(): Boolean {
        val now = Clock.System.now()
        return getForwardReferencePoint() < now
    }

    /**
     * There can be a gap between move-in date and the first tariff becomes effective.
     * We assume no smart meter logs before the first tariff start date.
     * We also accept when the end date is after the start date to make sure we show all available data.
     */
    fun canNavigateBackward(firstTariffStartDate: Instant): Boolean {
        val newReferencePoint = getBackwardReferencePoint()
        val newRequestPeriod = calculateQueryPeriod(
            referencePoint = newReferencePoint,
            presentationStyle = presentationStyle,
        )
        return newRequestPeriod.endInclusive >= firstTariffStartDate
    }

    /**
     * Generate the ConsumptionQueryFilter for making an API request
     */
    fun navigateBackward(firstTariffStartDate: Instant): ConsumptionQueryFilter? {
        if (!canNavigateBackward(firstTariffStartDate = firstTariffStartDate)) {
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
    private fun getBackwardReferencePoint(): Instant = when (presentationStyle) {
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

    private fun getForwardReferencePoint(): Instant = when (presentationStyle) {
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
