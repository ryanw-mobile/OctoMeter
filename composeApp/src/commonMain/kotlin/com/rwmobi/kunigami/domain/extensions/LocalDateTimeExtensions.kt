/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun LocalDateTime.startOfYear(): Instant {
    return LocalDate(year = year, monthNumber = 1, dayOfMonth = 1)
        .atTime(hour = 12, minute = 0) // Make it GMT-BST transition safe
        .toSystemDefaultTimeZoneInstant()
        .atStartOfDay()
}

fun LocalDateTime.endOfYear(): Instant {
    return LocalDate(year = year, monthNumber = 12, dayOfMonth = 31)
        .atTime(hour = 12, minute = 0) // Make it GMT-BST transition safe
        .toSystemDefaultTimeZoneInstant()
        .atEndOfDay()
}

fun LocalDateTime.startOfMonth(): Instant {
    return LocalDate(year = year, monthNumber = monthNumber, dayOfMonth = 1)
        .atTime(hour = 12, minute = 0) // Make it GMT-BST transition safe
        .toSystemDefaultTimeZoneInstant()
        .atStartOfDay()
}

fun LocalDateTime.endOfMonth(): Instant {
    return LocalDate(year = year, monthNumber = monthNumber, dayOfMonth = 1)
        .plus(1, DateTimeUnit.MONTH)
        .minus(1, DateTimeUnit.DAY)
        .atTime(hour = 12, minute = 0) // Make it GMT-BST transition safe
        .toSystemDefaultTimeZoneInstant()
        .atEndOfDay()
}

fun LocalDateTime.startOfWeek(): Instant {
    val dayOfWeek = date.dayOfWeek
    val daysSinceSunday = dayOfWeek.isoDayNumber
    return date
        .minus(value = daysSinceSunday - 1, unit = DateTimeUnit.DAY)
        .atTime(hour = 12, minute = 0) // Make it GMT-BST transition safe
        .toSystemDefaultTimeZoneInstant()
        .atStartOfDay()
}

fun LocalDateTime.endOfWeek(): Instant {
    val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - dayOfWeek.isoDayNumber
    return date
        .plus(daysUntilSunday, DateTimeUnit.DAY)
        .atTime(hour = 12, minute = 0) // Make it GMT-BST transition safe
        .toSystemDefaultTimeZoneInstant()
        .atEndOfDay()
}
