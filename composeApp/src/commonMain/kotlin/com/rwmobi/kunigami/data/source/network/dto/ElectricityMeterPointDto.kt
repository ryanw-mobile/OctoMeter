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
data class ElectricityMeterPointDto(
    @SerialName("mpan") val mpan: String,
    @SerialName("profile_class") val profileClass: Int,
    @SerialName("consumption_standard") val consumptionStandard: Int,
    @SerialName("meters") val meters: List<MeterDto>,
    @SerialName("agreements") val agreements: List<AgreementDto>,
    @SerialName("is_export") val isExport: Boolean,
)
