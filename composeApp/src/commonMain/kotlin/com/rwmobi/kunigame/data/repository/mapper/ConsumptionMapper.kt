/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.ConsumptionDto
import com.rwmobi.kunigami.domain.model.Consumption

fun ConsumptionDto.toConsumption() = Consumption(
    consumption = consumption,
    intervalStart = intervalStart,
    intervalEnd = intervalEnd,
)
