/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.products.ProductDetailsDto
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import kotlinx.datetime.Instant

@Deprecated("RestAPI implementation")
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
        // if (isChargedHalfHourly) add(ProductFeature.CHARGEDHALFHOURLY))
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
            if (isPrepay) add(ProductFeature.PREPAY)
            if (isBusiness) add(ProductFeature.BUSINESS)
            if (isChargedHalfHourly) add(ProductFeature.CHARGEDHALFHOURLY)
            // if (isTracker) add(ProductFeature.TRACKER)
            // if (isRestricted) add(ProductFeature.RESTRICTED)
        }.toList(),
        term = term,
        availability = Instant.parse(availableFrom.toString())..(availableTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE),
        brand = "OCTOPUS_ENERGY", // Filtered in GraphQL query
    )
}

fun SingleEnergyProductQuery.EnergyProduct.toProductDetails(): ProductDetails {
    val tariffNode = tariffs?.edges
        ?.firstOrNull()
        ?.node

    return ProductDetails(
        code = code,
        direction = ProductDirection.UNKNOWN,
        fullName = fullName,
        displayName = displayName,
        description = description,
        features = mutableListOf<ProductFeature>().apply {
            if (isVariable) add(ProductFeature.VARIABLE)
            if (isGreen) add(ProductFeature.GREEN)
            if (isPrepay) add(ProductFeature.PREPAY)
            if (isBusiness) add(ProductFeature.BUSINESS)
            if (isChargedHalfHourly) add(ProductFeature.CHARGEDHALFHOURLY)
            // if (isTracker) add(ProductFeature.TRACKER)
            // if (isRestricted) add(ProductFeature.RESTRICTED)
        }.toList(),
        term = term,
        availability = Instant.parse(availableFrom.toString())..(availableTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE),
        electricityTariff = when {
            tariffNode?.onStandardTariff?.tariffCode != null -> {
                Tariff(
                    productCode = code,
                    fullName = fullName,
                    displayName = displayName,
                    description = description,
                    isVariable = isVariable,
                    availability = Instant.parse(availableFrom.toString())..(availableTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE),
                    exitFeesType = ExitFeesType.fromApiValue(value = exitFeesType),
                    vatInclusiveExitFees = exitFees?.toDouble() ?: 0.0,
                    tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
                    tariffCode = tariffNode.onStandardTariff.tariffCode,
                    vatInclusiveStandingCharge = tariffNode.onStandardTariff.standingCharge ?: 0.0,
                    vatInclusiveStandardUnitRate = tariffNode.onStandardTariff.unitRate ?: 0.0,
                    vatInclusiveDayUnitRate = null,
                    vatInclusiveNightUnitRate = null,
                    vatInclusiveOffPeakRate = null,
                )
            }

            tariffNode?.onDayNightTariff?.tariffCode != null -> {
                Tariff(
                    productCode = code,
                    fullName = fullName,
                    displayName = displayName,
                    description = description,
                    isVariable = isVariable,
                    availability = Instant.parse(availableFrom.toString())..(availableTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE),
                    exitFeesType = ExitFeesType.fromApiValue(value = exitFeesType),
                    vatInclusiveExitFees = exitFees?.toDouble() ?: 0.0,
                    tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
                    tariffCode = tariffNode.onDayNightTariff.tariffCode,
                    vatInclusiveStandingCharge = tariffNode.onDayNightTariff.standingCharge ?: 0.0,
                    vatInclusiveDayUnitRate = tariffNode.onDayNightTariff.dayRate ?: 0.0,
                    vatInclusiveNightUnitRate = tariffNode.onDayNightTariff.nightRate ?: 0.0,
                    vatInclusiveOffPeakRate = null,
                    vatInclusiveStandardUnitRate = null,
                )
            }

            tariffNode?.onThreeRateTariff?.tariffCode != null -> {
                Tariff(
                    productCode = code,
                    fullName = fullName,
                    displayName = displayName,
                    description = description,
                    isVariable = isVariable,
                    availability = Instant.parse(availableFrom.toString())..(availableTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE),
                    exitFeesType = ExitFeesType.fromApiValue(value = exitFeesType),
                    vatInclusiveExitFees = exitFees?.toDouble() ?: 0.0,
                    tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
                    tariffCode = tariffNode.onThreeRateTariff.tariffCode,
                    vatInclusiveStandingCharge = tariffNode.onThreeRateTariff.standingCharge ?: 0.0,
                    vatInclusiveDayUnitRate = tariffNode.onThreeRateTariff.dayRate ?: 0.0,
                    vatInclusiveNightUnitRate = tariffNode.onThreeRateTariff.nightRate ?: 0.0,
                    vatInclusiveOffPeakRate = tariffNode.onThreeRateTariff.offPeakRate ?: 10.0,
                    vatInclusiveStandardUnitRate = null,
                )
            }

            else -> {
                // Currently we do not support other tariff types for simplicity
                null
            }
        },
        brand = "OCTOPUS_ENERGY", // Filtered in GraphQL query
    )
}
