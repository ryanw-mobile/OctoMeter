/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class ElectricityMeter(
    val serialNumber: String,
    val makeAndType: String?,
    val readingSource: String?,
    val readAt: Instant?,
    val value: Double?,
)
