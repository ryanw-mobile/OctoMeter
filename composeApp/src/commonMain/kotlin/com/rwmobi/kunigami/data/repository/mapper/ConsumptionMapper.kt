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

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.domain.extensions.roundConsumptionToNearestEvenHundredth
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.graphql.GetMeasurementsQuery
import com.rwmobi.kunigami.graphql.type.ReadingStatisticTypeEnum

fun ConsumptionEntity.toConsumptionWithCost() = ConsumptionWithCost(
    consumption = Consumption(
        kWhConsumed = kWhConsumed.roundConsumptionToNearestEvenHundredth(),
        interval = intervalStart..intervalEnd,
    ),
    vatInclusiveCost = consumptionCost,
    vatInclusiveStandingCharge = standingCharge,
)

fun GetMeasurementsQuery.Node.toConsumptionWithCost(): ConsumptionWithCost? {
    return if (onIntervalMeasurementType == null) {
        null
    } else {
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = value,
                interval = onIntervalMeasurementType.startAt..onIntervalMeasurementType.endAt,
            ),
            vatInclusiveCost = getEstimatedAmount(),
            vatInclusiveStandingCharge = getStandingCharge(),
        )
    }
}

fun GetMeasurementsQuery.Node.toConsumptionEntity(deviceId: String): ConsumptionEntity? {
    val estimatedAmount = getEstimatedAmount()
    val standingCharge = getStandingCharge()

    return if (onIntervalMeasurementType == null || estimatedAmount == null || standingCharge == null) {
        null
    } else {
        ConsumptionEntity(
            deviceId = deviceId,
            intervalStart = onIntervalMeasurementType.startAt,
            intervalEnd = onIntervalMeasurementType.endAt,
            kWhConsumed = value,
            consumptionCost = estimatedAmount,
            standingCharge = standingCharge,
        )
    }
}

private fun GetMeasurementsQuery.Node.getEstimatedAmount(): Double? {
    return metaData?.statistics
        ?.mapNotNull {
            if (it?.type == ReadingStatisticTypeEnum.TOU_BUCKET_COST ||
                it?.type == ReadingStatisticTypeEnum.CONSUMPTION_COST
            ) {
                it.costInclTax?.estimatedAmount
            } else {
                null
            }
        }
        ?.takeIf { it.isNotEmpty() } // Ensures null if no valid values exist
        ?.sum()
}

private fun GetMeasurementsQuery.Node.getStandingCharge(): Double? {
    return metaData?.statistics
        ?.mapNotNull {
            if (it?.type == ReadingStatisticTypeEnum.STANDING_CHARGE_COST) {
                it.costInclTax?.estimatedAmount
            } else {
                null
            }
        }
        ?.takeIf { it.isNotEmpty() } // Ensures null if no valid values exist
        ?.sum()
}
