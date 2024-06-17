/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.consumption

import androidx.compose.runtime.Immutable

@Immutable
data class Insights(
    val consumptionAggregateRounded: Double,
    val consumptionTimeSpan: Int,
    val consumptionChargeRatio: Double,
    val costWithCharges: Double,
    val isTrueCost: Boolean,
    val consumptionDailyAverage: Double,
    val costDailyAverage: Double,
    val consumptionAnnualProjection: Double,
    val costAnnualProjection: Double,
)
