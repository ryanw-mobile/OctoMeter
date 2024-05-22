/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.extensions

import android.icu.text.SimpleDateFormat
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

actual fun Instant.toLocalDateString(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault()).date
    val calendar = Calendar.getInstance().apply {
        set(localDate.year, localDate.monthNumber - 1, localDate.dayOfMonth)
    }
    val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
    return dateFormat.format(calendar.time)
}

actual fun Instant.toLocalMonthYear(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault()).date
    val calendar = Calendar.getInstance().apply {
        set(localDate.year, localDate.monthNumber - 1, 1) // Day of month is set to 1 as it's not needed
    }
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}
