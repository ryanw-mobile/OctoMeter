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
data class SampleQuotesDto(
    @SerialName("direct_debit_monthly") val directDebitMonthly: QuotesDto? = null,
    @SerialName("varying") val varying: QuotesDto? = null,
)
