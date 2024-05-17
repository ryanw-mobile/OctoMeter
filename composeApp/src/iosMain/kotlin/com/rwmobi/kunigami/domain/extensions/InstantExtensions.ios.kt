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
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun Instant.formatDate(): String {
    val dateFormatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterMediumStyle
        timeStyle = NSDateFormatterNoStyle
        locale = NSLocale.currentLocale
    }

    val localDate = toLocalDateTime(TimeZone.currentSystemDefault()).date
    val components = NSDateComponents().apply {
        year = localDate.year.toLong()
        month = localDate.monthNumber.toLong()
        day = localDate.dayOfMonth.toLong()
    }
    val calendar = NSCalendar.currentCalendar
    val date = calendar.dateFromComponents(components) ?: NSDate()
    return dateFormatter.stringFromDate(date)
}
