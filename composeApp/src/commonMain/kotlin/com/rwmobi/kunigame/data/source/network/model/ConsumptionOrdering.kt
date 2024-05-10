/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.data.source.network.model

enum class ConsumptionOrdering(val apiValue: String?) {
    PERIOD(apiValue = "period"),
    LATEST_FIRST(apiValue = null),
}
