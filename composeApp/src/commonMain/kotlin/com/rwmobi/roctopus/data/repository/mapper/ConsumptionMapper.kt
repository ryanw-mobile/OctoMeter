/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.repository.mapper

import com.rwmobi.roctopus.data.source.network.dto.ConsumptionDto
import com.rwmobi.roctopus.domain.model.Consumption

fun ConsumptionDto.toConsumption() = Consumption(
    consumption = consumption,
    intervalStart = intervalStart,
    intervalEnd = intervalEnd,
)
