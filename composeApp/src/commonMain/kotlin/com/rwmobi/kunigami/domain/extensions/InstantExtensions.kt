/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

// Time-zone aware conversion utils

fun Instant.toSystemDefaultLocalDateTime(): LocalDateTime {
    val currentZone = TimeZone.currentSystemDefault()
    return toLocalDateTime(timeZone = currentZone)
}

fun Instant.toSystemDefaultLocalDate(): LocalDate {
    val currentZone = TimeZone.currentSystemDefault()
    return toLocalDateTime(timeZone = currentZone).date
}

fun LocalDateTime.toSystemDefaultTimeZoneInstant(): Instant {
    val currentZone = TimeZone.currentSystemDefault()
    return toInstant(currentZone)
}

// Date time computation utils

/***
 * Converts this instant to the ISO 8601 string representation without seconds, for example, 2023-01-02T23:40Z.
 */
fun Instant.toIso8601WithoutSeconds(): String {
    val formatted = toString()
    // This assumes the format is always compliant with ISO-8601 extended format.
    return formatted.substring(0, formatted.lastIndexOf(':')) + "Z"
}

fun Instant.atStartOfHour(): Instant {
    val currentLocalDateTime = toSystemDefaultLocalDateTime()
    return LocalDateTime(
        date = currentLocalDateTime.date,
        time = LocalTime(
            hour = currentLocalDateTime.hour,
            minute = 0,
        ),
    ).toSystemDefaultTimeZoneInstant()
}

fun Instant.atStartOfDay(): Instant {
    val currentLocalDate = toSystemDefaultLocalDateTime().date
    return currentLocalDate.atStartOfDayIn(timeZone = TimeZone.currentSystemDefault())
}

fun Instant.atEndOfDay(): Instant {
    val currentLocalDate = toSystemDefaultLocalDateTime().date
    return currentLocalDate
        .plus(period = DatePeriod(days = 1))
        .atStartOfDayIn(timeZone = TimeZone.currentSystemDefault())
        .minus(value = 1, unit = DateTimeUnit.NANOSECOND)
}

fun Instant.getDayRange(): ClosedRange<Instant> = atStartOfDay()..atEndOfDay()

fun Instant.getWeekRange(): ClosedRange<Instant> {
    val localDateTime = toSystemDefaultLocalDateTime()
    return localDateTime.startOfWeek()..localDateTime.endOfWeek()
}

fun Instant.getMonthRange(): ClosedRange<Instant> {
    val localDateTime = toSystemDefaultLocalDateTime()
    return localDateTime.startOfMonth()..localDateTime.endOfMonth()
}

fun Instant.getYearRange(): ClosedRange<Instant> {
    val localDateTime = toSystemDefaultLocalDateTime()
    return localDateTime.startOfYear()..localDateTime.endOfYear()
}

/***
 * Returns the number of milliseconds to the upcoming 00 or 30 minutes.
 * This is for recurrent background tasks.
 */
fun Instant.getNextHalfHourCountdownMillis(): Long {
    val currentLocalDateTime = toSystemDefaultLocalDateTime()
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

// Formatting utils

fun Instant.getLocalYear(): Int {
    return toSystemDefaultLocalDateTime().year
}

/***
 * Returns the integer day of month as Integer. To avoid confusion, caller may apply padding manually if needed.
 */
fun Instant.getLocalDayOfMonth(): Int {
    val currentLocalDateTime = toSystemDefaultLocalDateTime()
    return currentLocalDateTime.dayOfMonth
}

/***
 * Returns the local time string with padding, that is, 00:00
 */
fun Instant.getLocalHHMMString(): String {
    val currentLocalDateTime = toSystemDefaultLocalDateTime()
    return "${currentLocalDateTime.hour.toString().padStart(2, '0')}:${currentLocalDateTime.minute.toString().padStart(2, '0')}"
}

/***
 * Returns the local hour string with padding, that is, 00
 */
fun Instant.getLocalHourString(): String {
    val currentLocalDateTime = toSystemDefaultLocalDateTime()
    return currentLocalDateTime.hour.toString().padStart(2, '0')
}

/***
 * Returns a concatenation of the platform-dependent getLocalDateString() and zero-padded HH:MM time string
 */
fun Instant.toLocalDateTimeString(): String {
    val currentLocalDateTime = toSystemDefaultLocalDateTime()
    return "${getLocalDateString()} ${currentLocalDateTime.hour.toString().padStart(2, '0')}:${currentLocalDateTime.minute.toString().padStart(2, '0')}"
}

fun Instant.getLocalEnglishAbbreviatedDayOfWeekName(): String {
    val localDate = toSystemDefaultLocalDate()
    val customFormat = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
    }
    return localDate.format(customFormat)
}

/***
 * Returns English abbreviated day of week name and day of month with padding. Example: Mon 02
 */
fun Instant.getLocalDayOfWeekAndDayString(): String {
    val localDate = toSystemDefaultLocalDate()
    val customFormat = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth(padding = Padding.ZERO)
    }
    return localDate.format(customFormat)
}

/***
 * Returns local day of month with padding and English abbreviated month. Example: 02 Feb
 */
fun Instant.getLocalDayMonthString(): String {
    val localDate = toSystemDefaultLocalDate()
    val customFormat = LocalDate.Format {
        dayOfMonth(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }
    return localDate.format(customFormat)
}

/***
 * Returns local month in abbreviated English
 */
fun Instant.getLocalMonthString(): String {
    val localDate = toSystemDefaultLocalDate()
    val customFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }
    return localDate.format(customFormat)
}

/***
 * Returns local month in abbreviated English and year. Example: Jan 2024
 */
fun Instant.getLocalMonthYearString(): String {
    val localDate = toSystemDefaultLocalDate()
    val customFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED) // Full month name
        char(' ')
        year()
    }
    return localDate.format(customFormat)
}

expect fun Instant.getLocalDateString(): String
