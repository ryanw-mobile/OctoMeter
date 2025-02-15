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

import co.touchlab.kermit.Logger
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
)

fun GetMeasurementsQuery.Node.toConsumptionWithCost(): ConsumptionWithCost? {
    Logger.v(tag = "toConsumptionWithCost", messageString = "metaData = $metaData")

    val estimatedAmount = metaData?.statistics?.firstOrNull {
        it?.type == ReadingStatisticTypeEnum.TOU_BUCKET_COST ||
            it?.type == ReadingStatisticTypeEnum.CONSUMPTION_COST
    }?.costInclTax?.estimatedAmount

    Logger.v(tag = "toConsumptionWithCost", messageString = "amount = $estimatedAmount")

    return if (onIntervalMeasurementType == null) {
        null
    } else {
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = value,
                interval = onIntervalMeasurementType.startAt..onIntervalMeasurementType.endAt,
            ),
            vatInclusiveCost = estimatedAmount,
        )
    }
}

fun GetMeasurementsQuery.Node.toConsumptionEntity(deviceId: String): ConsumptionEntity? {
    val estimatedAmount = metaData?.statistics?.firstOrNull {
        it?.type == ReadingStatisticTypeEnum.TOU_BUCKET_COST ||
            it?.type == ReadingStatisticTypeEnum.CONSUMPTION_COST
    }?.costInclTax?.estimatedAmount

    return if (onIntervalMeasurementType == null || estimatedAmount == null) {
        null
    } else {
        ConsumptionEntity(
            deviceId = deviceId,
            intervalStart = onIntervalMeasurementType.startAt,
            intervalEnd = onIntervalMeasurementType.endAt,
            kWhConsumed = value,
            consumptionCost = estimatedAmount,
        )
    }
}
