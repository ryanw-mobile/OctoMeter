/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import kotlinx.datetime.Instant

data class Agreement(
    val tariffCode: String,
    val validFrom: Instant,
    val validTo: Instant?,
)
