/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.domain.model

import kotlinx.datetime.Instant

data class Product(
    val code: String,
    val direction: ProductDirection,
    val fullName: String,
    val displayName: String,
    val description: String,
    val isVariable: Boolean,
    val isGreen: Boolean,
    val isTracker: Boolean,
    val isPrepay: Boolean,
    val isBusiness: Boolean,
    val isRestricted: Boolean,
    val term: Int?,
    val availableFrom: Instant,
    val availableTo: Instant?,
    // We don't know how to parse links for product details yet
    val brand: String,
)
