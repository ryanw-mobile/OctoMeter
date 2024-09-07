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

import com.rwmobi.kunigami.data.source.network.dto.LinkDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ElectricityTariffDto(
    @SerialName("code") val code: String,
    @SerialName("standing_charge_exc_vat") val standingChargeExcVat: Double,
    @SerialName("standing_charge_inc_vat") val standingChargeIncVat: Double,
    @SerialName("online_discount_exc_vat") val onlineDiscountExcVat: Double,
    @SerialName("online_discount_inc_vat") val onlineDiscountIncVat: Double,
    @SerialName("dual_fuel_discount_exc_vat") val dualFuelDiscountExcVat: Double,
    @SerialName("dual_fuel_discount_inc_vat") val dualFuelDiscountIncVat: Double,
    @SerialName("exit_fees_exc_vat") val exitFeesExcVat: Double,
    @SerialName("exit_fees_inc_vat") val exitFeesIncVat: Double,
    @SerialName("exit_fees_type") val exitFeesType: String,
    @SerialName("standard_unit_rate_exc_vat") val standardUnitRateExcVat: Double? = null,
    @SerialName("standard_unit_rate_inc_vat") val standardUnitRateIncVat: Double? = null,
    @SerialName("day_unit_rate_exc_vat") val dayUnitRateExcVat: Double? = null,
    @SerialName("day_unit_rate_inc_vat") val dayUnitRateIncVat: Double? = null,
    @SerialName("night_unit_rate_exc_vat") val nightUnitRateExcVat: Double? = null,
    @SerialName("night_unit_rate_inc_vat") val nightUnitRateIncVat: Double? = null,
    @SerialName("links") val links: List<LinkDto>,
)
