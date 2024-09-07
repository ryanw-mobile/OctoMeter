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
