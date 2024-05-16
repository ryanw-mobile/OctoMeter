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
    @SerialName("links") val links: List<LinkDto>,
)
