/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.repository.mapper

import com.rwmobi.roctopus.data.source.network.dto.ProductDetails
import com.rwmobi.roctopus.domain.model.Product
import com.rwmobi.roctopus.domain.model.ProductDirection

fun ProductDetails.toProduct(): Product {
    return Product(
        code = code,
        direction = ProductDirection.fromValue(direction),
        fullName = fullName,
        displayName = displayName,
        description = description,
        isVariable = isVariable,
        isGreen = isGreen,
        isTracker = isTracker,
        isPrepay = isPrepay,
        isBusiness = isBusiness,
        isRestricted = isRestricted,
        term = term,
        availableFrom = availableFrom,
        availableTo = availableTo,
        brand = brand,
    )
}

fun List<ProductDetails>.toProduct(): List<Product> = map { it.toProduct() }
