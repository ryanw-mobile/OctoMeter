/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto.account

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyDto(
    @SerialName("id") val id: Int,
    @SerialName("moved_in_at") val movedInAt: Instant?,
    @SerialName("moved_out_at") val movedOutAt: Instant?,
    @SerialName("address_line_1") val addressLine1: String,
    @SerialName("address_line_2") val addressLine2: String,
    @SerialName("address_line_3") val addressLine3: String,
    @SerialName("town") val town: String,
    @SerialName("county") val county: String,
    @SerialName("postcode") val postcode: String,
    @SerialName("electricity_meter_points") val electricityMeterPoints: List<ElectricityMeterPointDto>,
    @SerialName("gas_meter_points") val gasMeterPoints: List<GasMeterPointDto>,
)
