/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.consumption

enum class ConsumptionGrouping(val apiValue: String?) {
    // This aggregates half hours into days based on the local time, not the UTC time, so there will be one fewer hour included on the daylight savings in the spring.
    HALF_HOURLY(apiValue = null),
    DAY(apiValue = "day"),
    WEEK(apiValue = "week"),
    MONTH(apiValue = "month"),
    QUARTER(apiValue = "quarter"),
}
