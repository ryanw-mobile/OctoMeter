/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto.singleproduct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DualFuelConsumptionDetailDto(
    @SerialName("electricity_standard") val electricityStandard: Int,
    @SerialName("gas_standard") val gasStandard: Int,
)
