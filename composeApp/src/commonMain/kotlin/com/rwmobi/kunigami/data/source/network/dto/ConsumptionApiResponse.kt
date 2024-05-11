/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConsumptionApiResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ConsumptionDto>,
)
