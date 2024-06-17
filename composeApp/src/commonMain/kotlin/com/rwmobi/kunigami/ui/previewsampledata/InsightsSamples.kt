/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.previewsampledata

import com.rwmobi.kunigami.ui.model.consumption.Insights

internal object InsightsSamples {
    val trueCost = Insights(
        consumptionAggregateRounded = 85.115,
        consumptionTimeSpan = 303,
        consumptionChargeRatio = 0.64,
        costWithCharges = 90.988,
        consumptionDailyAverage = 32.611,
        costDailyAverage = 12.434,
        consumptionAnnualProjection = 30.235,
        costAnnualProjection = 11.487,
        isTrueCost = true,
    )
}
