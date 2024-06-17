/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.extensions.roundToNearestEvenHundredth
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.model.consumption.getConsumptionDaySpan
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.ui.model.consumption.Insights

class GenerateUsageInsightsUseCase {

    operator fun invoke(
        tariffSummary: TariffSummary?,
        consumptionWithCost: List<ConsumptionWithCost>?,
    ): Insights? {
        return if (tariffSummary == null || consumptionWithCost.isNullOrEmpty()) {
            null
        } else {
            val consumptionAggregateRounded = consumptionWithCost.sumOf { it.consumption.kWhConsumed }
            val consumptionTimeSpan = consumptionWithCost.map { it.consumption }.getConsumptionDaySpan()
            val isTrueCost = !consumptionWithCost.any { it.vatInclusiveCost == null }
            val consumptionCharge = if (isTrueCost) {
                consumptionWithCost.sumOf { it.vatInclusiveCost ?: 0.0 }
            } else {
                consumptionAggregateRounded * tariffSummary.vatInclusiveUnitRate
            }
            val costWithCharges = ((consumptionTimeSpan * tariffSummary.vatInclusiveStandingCharge) + consumptionCharge) / 100.0
            val consumptionChargeRatio = (consumptionCharge / 100.0) / costWithCharges
            val consumptionDailyAverage = consumptionWithCost.sumOf { it.consumption.kWhConsumed } / consumptionWithCost.map { it.consumption }.getConsumptionDaySpan()
            val costDailyAverage = (tariffSummary.vatInclusiveStandingCharge + consumptionDailyAverage * tariffSummary.vatInclusiveUnitRate) / 100.0
            val consumptionAnnualProjection = consumptionWithCost.sumOf { it.consumption.kWhConsumed } / consumptionTimeSpan * 365
            val costAnnualProjection = (tariffSummary.vatInclusiveStandingCharge * 365 + consumptionAnnualProjection * consumptionCharge / consumptionAggregateRounded) / 100.0

            Insights(
                consumptionAggregateRounded = consumptionAggregateRounded.roundToNearestEvenHundredth(),
                consumptionTimeSpan = consumptionTimeSpan,
                consumptionChargeRatio = consumptionChargeRatio.roundToTwoDecimalPlaces(),
                costWithCharges = costWithCharges.roundToTwoDecimalPlaces(),
                isTrueCost = isTrueCost,
                consumptionDailyAverage = consumptionDailyAverage.roundToNearestEvenHundredth(),
                costDailyAverage = costDailyAverage.roundToTwoDecimalPlaces(),
                consumptionAnnualProjection = consumptionAnnualProjection.roundToNearestEvenHundredth(),
                costAnnualProjection = costAnnualProjection.roundToTwoDecimalPlaces(),
            )
        }
    }
}
