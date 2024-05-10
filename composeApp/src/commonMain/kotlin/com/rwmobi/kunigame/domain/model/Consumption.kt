/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.domain.model

import kotlinx.datetime.Instant

data class Consumption(
    val consumption: Double,
    val intervalStart: Instant,
    val intervalEnd: Instant,
)
