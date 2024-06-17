/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.consumption.ConsumptionDto
import com.rwmobi.kunigami.domain.extensions.roundToNearestEvenHundredth
import com.rwmobi.kunigami.domain.model.consumption.Consumption

fun ConsumptionDto.toConsumption() = Consumption(
    kWhConsumed = consumption.roundToNearestEvenHundredth(),
    intervalStart = intervalStart,
    intervalEnd = intervalEnd,
)
