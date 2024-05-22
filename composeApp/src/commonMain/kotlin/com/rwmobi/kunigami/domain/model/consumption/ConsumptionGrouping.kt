/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.consumption

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.nanoseconds

enum class ConsumptionGrouping(val apiValue: String?) {
    // This aggregates half hours into days based on the local time, not the UTC time, so there will be one fewer hour included on the daylight savings in the spring.
    HALF_HOURLY(apiValue = null),
    DAY(apiValue = "day"),
    WEEK(apiValue = "week"),
    MONTH(apiValue = "month"),
    QUARTER(apiValue = "quarter"),
    ;

    fun calculateStartDate(pointOfReference: Instant): Instant {
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = pointOfReference.toLocalDateTime(timeZone)

        return when (this) {
            HALF_HOURLY, DAY -> {
                val startOfDay = localDateTime.date.atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                startOfDay.toInstant(timeZone)
            }

            WEEK -> {
                val dayOfWeek = localDateTime.date.dayOfWeek
                val daysSinceSunday = dayOfWeek.isoDayNumber
                val startOfWeek = localDateTime.date
                    .minus(value = daysSinceSunday - 1, unit = DateTimeUnit.DAY)
                    .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                startOfWeek.toInstant(timeZone)
            }

            MONTH -> {
                val startOfThisMonth = LocalDate(year = localDateTime.year, monthNumber = localDateTime.monthNumber, dayOfMonth = 1)
                    .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                startOfThisMonth.toInstant(timeZone)
            }

            QUARTER -> {
                val currentMonth = localDateTime.date.monthNumber
                val startOfQuarterMonth = when (currentMonth) {
                    in 1..3 -> Month.JANUARY
                    in 4..6 -> Month.APRIL
                    in 7..9 -> Month.JULY
                    else -> Month.OCTOBER
                }
                val firstDayOfQuarter = LocalDate(year = localDateTime.year, month = startOfQuarterMonth, dayOfMonth = 1)
                    .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
                firstDayOfQuarter.toInstant(timeZone)
            }
        }
    }

    fun calculateEndDate(pointOfReference: Instant): Instant {
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = pointOfReference.toLocalDateTime(timeZone)

        return when (this) {
            HALF_HOURLY, DAY -> {
                val endOfDay = localDateTime.date.atTime(hour = 23, minute = 59, second = 59, nanosecond = 999999999)
                endOfDay.toInstant(timeZone)
            }

            WEEK -> {
                val dayOfWeek = localDateTime.date.dayOfWeek
                val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - dayOfWeek.isoDayNumber
                val endOfWeek = localDateTime.date.plus(daysUntilSunday, DateTimeUnit.DAY)
                    .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999999999)
                endOfWeek.toInstant(timeZone)
            }

            MONTH -> {
                val startOfNextMonth = LocalDate(year = localDateTime.year, monthNumber = localDateTime.monthNumber, dayOfMonth = 1)
                    .plus(1, DateTimeUnit.MONTH)
                val endOfMonth = startOfNextMonth.atStartOfDayIn(timeZone) - 1.nanoseconds
                endOfMonth
            }

            QUARTER -> {
                val currentMonth = localDateTime.date.monthNumber
                val endOfQuarterMonth = when (currentMonth) {
                    in 1..3 -> Month.MARCH
                    in 4..6 -> Month.JUNE
                    in 7..9 -> Month.SEPTEMBER
                    else -> Month.DECEMBER
                }
                val lastDayOfQuarter = LocalDate(year = localDateTime.year, month = endOfQuarterMonth, dayOfMonth = 1)
                    .plus(value = 1, unit = DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).dayOfMonth
                val endOfQuarter = LocalDate(year = localDateTime.year, month = endOfQuarterMonth, dayOfMonth = lastDayOfQuarter)
                    .atTime(hour = 23, minute = 59, second = 59, nanosecond = 999999999)
                endOfQuarter.toInstant(timeZone)
            }
        }
    }
}
