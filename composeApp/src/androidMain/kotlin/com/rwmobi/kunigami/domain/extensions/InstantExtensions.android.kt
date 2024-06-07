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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

actual fun Instant.getLocalDateString(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault()).date
    val javaDate = java.time.LocalDate.of(localDate.year, localDate.monthNumber, localDate.dayOfMonth)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
    return javaDate.format(formatter)
}
