/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.model

enum class RateType(val id: Int) {
    STANDING_CHARGE(id = 0),
    STANDARD_UNIT_RATE(id = 1),
    DAY_UNIT_RATE(id = 2),
    NIGHT_UNIT_RATE(id = 3),
}
