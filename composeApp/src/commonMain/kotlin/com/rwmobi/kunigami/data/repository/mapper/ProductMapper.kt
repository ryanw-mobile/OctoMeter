/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.products.ProductDetailsDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.domain.model.product.ElectricityTariffType
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import kotlinx.datetime.Instant

fun ProductDetailsDto.toProductSummary() = ProductSummary(
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
    availability = availableFrom..(availableTo ?: Instant.DISTANT_FUTURE),
    brand = brand,
)

fun EnergyProductsQuery.Node.toProductSummary(): ProductSummary {
    return ProductSummary(
        code = code,
        direction = ProductDirection.fromApiValue(direction?.rawValue),
        fullName = fullName,
        displayName = displayName,
        description = description,
        features = mutableListOf<ProductFeature>().apply {
            if (isVariable) add(ProductFeature.VARIABLE)
            if (isGreen) add(ProductFeature.GREEN)
            // if (isTracker) add(ProductFeature.TRACKER)
            if (isPrepay) add(ProductFeature.PREPAY)
            if (isBusiness) add(ProductFeature.BUSINESS)
            //  if (isRestricted) add(ProductFeature.RESTRICTED)
        }.toList(),
        term = term,
        availability = Instant.parse(availableFrom.toString())..(availableTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE),
        brand = "OCTOPUS_ENERGY", // Filtered in GraphQL query
    )
}

fun SingleProductApiResponse.toProductDetails(): ProductDetails {
    return ProductDetails(
        code = code,
        direction = ProductDirection.UNKNOWN,
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
        availability = availableFrom..(availableTo ?: Instant.DISTANT_FUTURE),
        electricityTariffType = when {
            singleRegisterElectricityTariffs.isNotEmpty() -> ElectricityTariffType.SINGLE_REGISTER
            dualRegisterElectricityTariffs.isNotEmpty() -> ElectricityTariffType.DUAL_REGISTER
            else -> ElectricityTariffType.UNKNOWN
        },
        // TODO: This is not a very good implementation - Need to refactor when support dual rates
        electricityTariffs = when {
            singleRegisterElectricityTariffs.isNotEmpty() -> singleRegisterElectricityTariffs.mapNotNull { (key, value) ->
                val tariffCode = value.varying?.code ?: value.directDebitMonthly?.code
                tariffCode?.let {
                    key to toTariff(tariffCode = tariffCode)
                }
            }.toMap()

            dualRegisterElectricityTariffs.isNotEmpty() -> dualRegisterElectricityTariffs.mapNotNull { (key, value) ->
                val tariffCode = value.varying?.code ?: value.directDebitMonthly?.code
                tariffCode?.let {
                    key to toTariff(tariffCode = tariffCode)
                }
            }.toMap()

            else -> null
        },
        brand = brand,
    )
}
