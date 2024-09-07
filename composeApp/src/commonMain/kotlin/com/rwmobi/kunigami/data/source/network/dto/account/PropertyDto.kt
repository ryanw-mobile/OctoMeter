/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
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
