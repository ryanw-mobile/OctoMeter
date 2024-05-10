/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.domain.model

import kotlinx.datetime.Instant

data class Account(
    val id: Int,
    val movedInAt: Instant?,
    val movedOutAt: Instant?,
    val addressLine1: String,
    val addressLine2: String,
    val addressLine3: String,
    val town: String,
    val county: String,
    val postcode: String,
)
