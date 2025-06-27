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

package com.rwmobi.kunigami.data.source.network.dto.products

import com.rwmobi.kunigami.data.source.network.dto.LinkDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.TariffDetailsDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ProductDetailsDto(
    @SerialName("code") val code: String,
    @SerialName("direction") val direction: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("description") val description: String,
    @SerialName("is_variable") val isVariable: Boolean,
    @SerialName("is_green") val isGreen: Boolean,
    @SerialName("is_tracker") val isTracker: Boolean,
    @SerialName("is_prepay") val isPrepay: Boolean,
    @SerialName("is_business") val isBusiness: Boolean,
    @SerialName("is_restricted") val isRestricted: Boolean,
    @SerialName("term") val term: Int?,
    @SerialName("available_from") val availableFrom: Instant,
    @SerialName("available_to") val availableTo: Instant?,
    @SerialName("single_register_electricity_tariffs") val singleRegisterElectricityTariffs: Map<String, TariffDetailsDto>? = null,
    @SerialName("dual_register_electricity_tariffs") val dualRegisterElectricityTariffs: Map<String, TariffDetailsDto>? = null,
    @SerialName("links") val linkDtos: List<LinkDto>,
    @SerialName("brand") val brand: String,
)
