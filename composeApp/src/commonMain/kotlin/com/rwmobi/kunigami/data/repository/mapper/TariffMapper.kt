/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.domain.exceptions.TariffNotFoundException
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import kotlinx.datetime.Instant

fun SingleProductApiResponse.toTariff(
    tariffCode: String,
): Tariff {
    // SingleProductApiResponse comes with all retail regions
    // We filter the one requested
    val retailRegionTariff = when {
        tariffCode.startsWith("E-1R") -> {
            singleRegisterElectricityTariffs["_${tariffCode[tariffCode.lastIndex]}"]
        }

        tariffCode.startsWith("E-2R") -> {
            dualRegisterElectricityTariffs["_${tariffCode[tariffCode.lastIndex]}"]
        }

        else -> {
            throw IllegalArgumentException("$tariffCode not found in product $code")
        }
    }

    if (retailRegionTariff == null) {
        throw IllegalArgumentException("$tariffCode not found in product $code")
    }

    val rates = retailRegionTariff.varying ?: retailRegionTariff.directDebitMonthly ?: throw TariffNotFoundException(tariffCode)
    val tariffPaymentTerm = when {
        retailRegionTariff.directDebitMonthly != null -> TariffPaymentTerm.DIRECT_DEBIT_MONTHLY
        retailRegionTariff.varying != null -> TariffPaymentTerm.VARYING
        else -> TariffPaymentTerm.UNKNOWN
    }

    return Tariff(
        productCode = code,
        fullName = fullName,
        displayName = displayName,
        description = description,
        isVariable = isVariable,
        availability = availableFrom..(availableTo ?: Instant.DISTANT_FUTURE),

        tariffCode = rates.code,
        tariffPaymentTerm = tariffPaymentTerm,
        vatInclusiveStandingCharge = rates.standingChargeIncVat,
        exitFeesType = ExitFeesType.fromApiValue(value = rates.exitFeesType),
        vatInclusiveExitFees = rates.exitFeesIncVat,
        vatInclusiveStandardUnitRate = rates.standardUnitRateIncVat,
        vatInclusiveDayUnitRate = rates.dayUnitRateIncVat,
        vatInclusiveNightUnitRate = rates.nightUnitRateIncVat,
    )
}

fun SingleEnergyProductQuery.EnergyProduct.toTariff(
    tariffCode: String,
): Tariff? {
    val tariffNode = tariffs?.edges
        ?.firstOrNull { it?.node?.onStandardTariff?.tariffCode == tariffCode }
        ?.node

    if (tariffNode == null) {
        return null
    }

    // In GraphQL currently we default to direct debit
    val tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY

    return when {
        tariffNode.onStandardTariff != null -> {
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

                tariffCode = tariffCode,
                vatInclusiveStandingCharge = tariffNode.onStandardTariff.standingCharge ?: 0.0,
                vatInclusiveStandardUnitRate = tariffNode.onStandardTariff.unitRate ?: 0.0,
                vatInclusiveDayUnitRate = null,
                vatInclusiveNightUnitRate = null,
            )
        }

        else -> {
            // Currently we do not support other tariff types for simplicity
            null
        }
    }
}
