/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.domain.repository

import com.rwmobi.roctopus.domain.model.Product

interface OctopusRepository {
    suspend fun getProducts(): Result<List<Product>>
}
