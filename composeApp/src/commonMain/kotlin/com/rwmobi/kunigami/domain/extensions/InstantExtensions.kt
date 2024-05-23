/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Instant.formatInstantWithoutSeconds(): String {
    val formatted = toString()
    // This assumes the format is always compliant with ISO-8601 extended format.
    return formatted.substring(0, formatted.lastIndexOf(':')) + "Z"
}

fun Instant.roundDownToHour(): Instant {
    val currentZone = TimeZone.currentSystemDefault()
    val currentLocalDateTime = toLocalDateTime(timeZone = currentZone)
    return LocalDateTime(
        year = currentLocalDateTime.year,
        month = currentLocalDateTime.month,
        dayOfMonth = currentLocalDateTime.dayOfMonth,
        hour = currentLocalDateTime.hour,
        minute = 0,
        second = 0,
        nanosecond = 0,
    ).toInstant(timeZone = currentZone)
}

/***
 * Return the Instant with the time portion set to all 0
 */
fun Instant.roundDownToDay(): Instant {
    val currentZone = TimeZone.currentSystemDefault()
    val currentLocalDateTime = toLocalDateTime(timeZone = currentZone)
    return LocalDateTime(
        year = currentLocalDateTime.year,
        month = currentLocalDateTime.month,
        dayOfMonth = currentLocalDateTime.dayOfMonth,
        hour = 0,
        minute = 0,
        second = 0,
        nanosecond = 0,
    ).toInstant(timeZone = currentZone)
}

fun Instant.roundUpToDayEnd(): Instant {
    val currentZone = TimeZone.currentSystemDefault()
    val currentLocalDateTime = toLocalDateTime(timeZone = currentZone)
    return LocalDateTime(
        year = currentLocalDateTime.year,
        month = currentLocalDateTime.month,
        dayOfMonth = currentLocalDateTime.dayOfMonth,
        hour = 23,
        minute = 59,
        second = 59,
        nanosecond = 999_999_999,
    ).toInstant(timeZone = currentZone)
}

fun Instant.toLocalHourMinuteString(): String {
    val currentLocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${currentLocalDateTime.hour.toString().padStart(2, '0')}:${currentLocalDateTime.minute.toString().padStart(2, '0')}"
}

fun Instant.toLocalHourString(): String {
    val currentLocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return currentLocalDateTime.hour.toString().padStart(2, '0')
}

fun Instant.toLocalDateTimeString(): String {
    val currentLocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${toLocalDateString()} ${currentLocalDateTime.hour.toString().padStart(2, '0')}:${currentLocalDateTime.minute.toString().padStart(2, '0')}"
}

fun Instant.toLocalYear(): String {
    val currentLocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return currentLocalDateTime.year.toString()
}

fun Instant.toLocalDay(): String {
    val currentLocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return currentLocalDateTime.dayOfMonth.toString()
}

fun Instant.toLocalWeekday(): String {
    val localDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val customFormat = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
    }
    return localDate.format(customFormat)
}

fun Instant.toLocalWeekdayDay(): String {
    val localDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val customFormat = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth(padding = Padding.ZERO)
    }
    return localDate.format(customFormat)
}

fun Instant.toLocalDayMonth(): String {
    val localDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val customFormat = LocalDate.Format {
        dayOfMonth(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }
    return localDate.format(customFormat)
}

fun Instant.toLocalMonth(): String {
    val localDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val customFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }
    return localDate.format(customFormat)
}

fun Instant.toLocalMonthYear(): String {
    val localDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val customFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED) // Full month name
        char(' ')
        year()
    }
    return localDate.format(customFormat)
}

/***
 * This is for recurrent background tasks that we want to trigger at 00 and 30 minutes.
 */
fun Instant.getNextHalfHourCountdownMillis(): Long {
    val currentLocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    val currentMinute = currentLocalDateTime.minute
    val currentSecond = currentLocalDateTime.second
    val currentNanosecond = currentLocalDateTime.nanosecond

    val minutesToNextTarget = if (currentMinute < 30) {
        30 - currentMinute
    } else {
        60 - currentMinute
    }
    val delayInMillis = (minutesToNextTarget * 60 * 1000) - (currentSecond * 1000) - (currentNanosecond / 1_000_000)

    return delayInMillis.toLong()
}

expect fun Instant.toLocalDateString(): String
