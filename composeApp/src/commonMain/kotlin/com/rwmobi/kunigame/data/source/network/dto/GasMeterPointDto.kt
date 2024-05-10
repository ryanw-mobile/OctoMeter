/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.data.source.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GasMeterPointDto(
    @SerialName("mprn") val mprn: String,
    @SerialName("consumption_standard") val consumptionStandard: Int,
    @SerialName("meters") val meters: List<MeterDto>,
    @SerialName("agreements") val agreements: List<AgreementDto>,
)
