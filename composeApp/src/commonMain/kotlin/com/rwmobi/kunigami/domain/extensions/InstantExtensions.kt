/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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

expect fun Instant.toLocalMonthYear(): String
expect fun Instant.toLocalDateString(): String
