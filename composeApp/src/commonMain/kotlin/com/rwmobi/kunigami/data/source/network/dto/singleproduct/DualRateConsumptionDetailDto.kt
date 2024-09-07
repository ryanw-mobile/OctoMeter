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

package com.rwmobi.kunigami.data.source.network.dto.singleproduct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DualRateConsumptionDetailDto(
    @SerialName("electricity_day") val electricityDay: Int,
    @SerialName("electricity_night") val electricityNight: Int,
    @SerialName("gas_standard") val gasStandard: Int? = null,
)
