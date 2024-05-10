/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.data.repository.mapper

import com.rwmobi.kunigame.data.source.network.dto.ConsumptionDto
import com.rwmobi.kunigame.domain.model.Consumption

fun ConsumptionDto.toConsumption() = Consumption(
    consumption = consumption,
    intervalStart = intervalStart,
    intervalEnd = intervalEnd,
)
