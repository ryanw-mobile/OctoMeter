/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.consumption

enum class ConsumptionDataOrder(val apiValue: String?) {
    PERIOD(apiValue = "period"),
    LATEST_FIRST(apiValue = null),
}
