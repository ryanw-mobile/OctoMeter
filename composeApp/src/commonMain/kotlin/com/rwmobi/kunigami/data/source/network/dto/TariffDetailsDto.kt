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
data class TariffDetailsDto(
    @SerialName("direct_debit_monthly") val directDebitMonthly: ElectricityTariffDto? = null,
    @SerialName("varying") val varying: ElectricityTariffDto? = null,
)
