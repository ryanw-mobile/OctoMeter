/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumptionDto(
    @SerialName("consumption") val consumption: Double,
    @SerialName("interval_start") val intervalStart: Instant,
    @SerialName("interval_end") val intervalEnd: Instant,
)
