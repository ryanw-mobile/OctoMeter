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
data class DualRateConsumptionDetailDto(
    @SerialName("electricity_day") val electricityDay: Int,
    @SerialName("electricity_night") val electricityNight: Int,
    @SerialName("gas_standard") val gasStandard: Int? = null,
)
