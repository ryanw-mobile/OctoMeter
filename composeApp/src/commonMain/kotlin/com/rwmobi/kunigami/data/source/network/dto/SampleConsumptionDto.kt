/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SampleConsumptionDto(
    @SerialName("electricity_single_rate") val electricitySingleRate: ConsumptionDetailDto,
    @SerialName("electricity_dual_rate") val electricityDualRate: DualRateConsumptionDetailDto,
    @SerialName("dual_fuel_single_rate") val dualFuelSingleRate: DualFuelConsumptionDetailDto,
    @SerialName("dual_fuel_dual_rate") val dualFuelDualRate: DualRateConsumptionDetailDto,
)
