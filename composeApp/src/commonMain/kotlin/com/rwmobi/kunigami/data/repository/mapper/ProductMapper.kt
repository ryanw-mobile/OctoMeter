/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
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
import kotlin.time.Instant

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
    // In GraphQL currently we default to direct debit
    val tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY

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
                    tariffPaymentTerm = tariffPaymentTerm,
                    tariffCode = tariffNode.onStandardTariff.tariffCode,
                    vatInclusiveStandingCharge = tariffNode.onStandardTariff.standingCharge ?: 0.0,
                    vatInclusiveStandardUnitRate = tariffNode.onStandardTariff.unitRate,
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
                    tariffPaymentTerm = tariffPaymentTerm,
                    tariffCode = tariffNode.onDayNightTariff.tariffCode,
                    vatInclusiveStandingCharge = tariffNode.onDayNightTariff.standingCharge ?: 0.0,
                    vatInclusiveDayUnitRate = tariffNode.onDayNightTariff.dayRate,
                    vatInclusiveNightUnitRate = tariffNode.onDayNightTariff.nightRate,
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
                    tariffPaymentTerm = tariffPaymentTerm,
                    tariffCode = tariffNode.onThreeRateTariff.tariffCode,
                    vatInclusiveStandingCharge = tariffNode.onThreeRateTariff.standingCharge ?: 0.0,
                    vatInclusiveDayUnitRate = tariffNode.onThreeRateTariff.dayRate,
                    vatInclusiveNightUnitRate = tariffNode.onThreeRateTariff.nightRate,
                    vatInclusiveOffPeakRate = tariffNode.onThreeRateTariff.offPeakRate,
                    vatInclusiveStandardUnitRate = null,
                )
            }

            else -> {
                null
            }
        },
        brand = "OCTOPUS_ENERGY", // Filtered in GraphQL query
    )
}
