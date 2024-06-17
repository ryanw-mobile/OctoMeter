/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.consumption

import androidx.compose.runtime.Immutable

@Immutable
data class ConsumptionWithCost(
    val consumption: Consumption,
    val vatInclusiveCost: Double?,
)
