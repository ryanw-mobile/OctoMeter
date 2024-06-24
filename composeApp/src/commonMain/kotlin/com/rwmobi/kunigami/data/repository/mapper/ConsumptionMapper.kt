/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.network.dto.consumption.ConsumptionDto
import com.rwmobi.kunigami.domain.extensions.roundToNearestEvenHundredth
import com.rwmobi.kunigami.domain.model.consumption.Consumption

fun ConsumptionDto.toConsumption() = Consumption(
    kWhConsumed = consumption.roundToNearestEvenHundredth(),
    interval = intervalStart..intervalEnd,
)

fun ConsumptionDto.toConsumptionEntity(meterSerial: String) = ConsumptionEntity(
    meterSerial = meterSerial,
    intervalStart = intervalStart,
    intervalEnd = intervalEnd,
    kWhConsumed = consumption, // keep raw figures - caller do rounding
)

fun ConsumptionEntity.toConsumption() = Consumption(
    kWhConsumed = kWhConsumed.roundToNearestEvenHundredth(),
    interval = intervalStart..intervalEnd,
)
