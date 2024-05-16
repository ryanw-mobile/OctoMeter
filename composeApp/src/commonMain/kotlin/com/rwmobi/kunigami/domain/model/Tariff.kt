/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Tariff(
    val code: String,
    val fullName: String,
    val displayName: String,
    val vatInclusiveUnitRate: Double,
    val vatInclusiveStandingCharge: Double,
)
