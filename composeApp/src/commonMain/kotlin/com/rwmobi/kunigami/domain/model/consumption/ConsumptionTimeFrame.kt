/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.domain.model.consumption

enum class ConsumptionTimeFrame(val apiValue: String) {
    // String mapped from GraphQL ReadingFrequencyType
    // This aggregates half hours into days based on the local time, not the UTC time, so there will be one fewer hour included on the daylight savings in the spring.
    HALF_HOURLY(apiValue = "THIRTY_MIN_INTERVAL"),
    DAY(apiValue = "DAY_INTERVAL"),
    WEEK(apiValue = "WEEK_INTERVAL"),
    MONTH(apiValue = "MONTH_INTERVAL"),
    QUARTER(apiValue = "QUARTER_INTERVAL"),
}
