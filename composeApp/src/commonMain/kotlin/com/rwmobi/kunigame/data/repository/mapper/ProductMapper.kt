/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.ProductDetailsDto
import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.domain.model.ProductDirection
import com.rwmobi.kunigami.domain.model.ProductFeature

fun ProductDetailsDto.toProduct() = Product(
    code = code,
    direction = ProductDirection.fromApiValue(direction),
    fullName = fullName,
    displayName = displayName,
    description = description,
    features = mutableListOf<ProductFeature>().apply {
        if (isVariable) add(ProductFeature.VARIABLE)
        if (isGreen) add(ProductFeature.GREEN)
        if (isTracker) add(ProductFeature.TRACKER)
        if (isPrepay) add(ProductFeature.PREPAY)
        if (isBusiness) add(ProductFeature.BUSINESS)
        if (isRestricted) add(ProductFeature.RESTRICTED)
    }.toList(),
    term = term,
    availableFrom = availableFrom,
    availableTo = availableTo,
    brand = brand,
)
