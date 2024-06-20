/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class ProductSummary(
    val code: String,
    val direction: ProductDirection,
    val fullName: String,
    val displayName: String,
    val description: String,
    val features: List<ProductFeature>,
    val term: Int?,
    val availability: ClosedRange<Instant>,
    val brand: String,
)
