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

package com.rwmobi.kunigami.domain.usecase.consumption

import com.rwmobi.kunigami.domain.extensions.roundConsumptionToNearestEvenHundredth
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.model.consumption.getConsumptionDaySpan
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.model.consumption.Insights

class GenerateUsageInsightsUseCase {

    operator fun invoke(
        tariff: Tariff?,
        consumptionWithCost: List<ConsumptionWithCost>?,
    ): Insights? {
        return if (tariff == null || consumptionWithCost.isNullOrEmpty()) {
            null
        } else {
            val consumptionAggregated = consumptionWithCost.sumOf { it.consumption.kWhConsumed }
            val consumptionDaySpan = consumptionWithCost.map { it.consumption }.getConsumptionDaySpan()
            val isTrueCost = !consumptionWithCost.any { it.vatInclusiveCost == null }

            val consumptionCharge = if (isTrueCost) {
                consumptionWithCost.sumOf { it.vatInclusiveCost ?: 0.0 }
            } else {
                consumptionAggregated * (tariff.resolveUnitRate() ?: 0.0)
            }

            // if consumption range is a single day OR data missing from ConsumptionWithCost, we apply the provided tariff standing charge.
            val hasValidStandingCharge = (consumptionDaySpan > 1) && !consumptionWithCost.any { it.vatInclusiveStandingCharge == null }
            val standingCharge = if (hasValidStandingCharge) {
                consumptionWithCost.sumOf { it.vatInclusiveStandingCharge ?: 0.0 }
            } else {
                tariff.vatInclusiveStandingCharge * consumptionDaySpan
            }

            val costWithCharges = (standingCharge + consumptionCharge) / 100.0
            val consumptionChargeRatio = (consumptionCharge / 100.0) / costWithCharges
            val consumptionDailyAverage = consumptionAggregated / consumptionDaySpan
            val costDailyAverage = costWithCharges / consumptionDaySpan
            val consumptionAnnualProjection = consumptionDailyAverage * 365
            val costAnnualProjection = costDailyAverage * 365

            Insights(
                consumptionAggregateRounded = consumptionAggregated.roundConsumptionToNearestEvenHundredth(),
                consumptionTimeSpan = consumptionDaySpan,
                consumptionChargeRatio = consumptionChargeRatio.roundToTwoDecimalPlaces(),
                costWithCharges = costWithCharges.roundToTwoDecimalPlaces(),
                isTrueCost = isTrueCost,
                consumptionDailyAverage = consumptionDailyAverage.roundConsumptionToNearestEvenHundredth(),
                costDailyAverage = costDailyAverage.roundToTwoDecimalPlaces(),
                consumptionAnnualProjection = consumptionAnnualProjection.roundConsumptionToNearestEvenHundredth(),
                costAnnualProjection = costAnnualProjection.roundToTwoDecimalPlaces(),
            )
        }
    }
}
