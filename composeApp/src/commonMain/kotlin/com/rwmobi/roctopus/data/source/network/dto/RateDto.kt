/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.source.network.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RateDto(
    @SerialName("value_exc_vat") val vatExclusivePrice: Double,
    @SerialName("value_inc_vat") val vatInclusivePrice: Double,
    @SerialName("valid_from") val validFrom: Instant,
    @SerialName("valid_to") val validTo: Instant?,
    @SerialName("payment_method") val paymentMethod: String?,
)
