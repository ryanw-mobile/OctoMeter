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
data class ElectricityRateQuotesDto(
    @SerialName("annual_cost_inc_vat") val annualCostIncVat: Int,
    @SerialName("annual_cost_exc_vat") val annualCostExcVat: Int,
)
