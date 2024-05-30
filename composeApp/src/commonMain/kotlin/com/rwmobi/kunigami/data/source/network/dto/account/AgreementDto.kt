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
data class AgreementDto(
    @SerialName("tariff_code") val tariffCode: String,
    @SerialName("valid_from") val validFrom: Instant,
    @SerialName("valid_to") val validTo: Instant?,
)
