/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale

actual fun Instant.toLocalDateString(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault()).date
    val javaDate = java.time.LocalDate.of(localDate.year, localDate.monthNumber, localDate.dayOfMonth)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
    return javaDate.format(formatter)
}

actual fun Instant.toLocalMonthYear(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault()).date
    val calendar = Calendar.getInstance().apply {
        set(localDate.year, localDate.monthNumber - 1, 1) // Day of month is set to 1 as it's not needed
    }
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}
