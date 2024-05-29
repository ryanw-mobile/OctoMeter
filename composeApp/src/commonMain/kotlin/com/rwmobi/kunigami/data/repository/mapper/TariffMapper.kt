/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.TariffDetailsDto
import com.rwmobi.kunigami.domain.exceptions.TariffNotFoundException
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.TariffDetails
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import com.rwmobi.kunigami.domain.model.product.TariffSummary

fun SingleProductApiResponse.toTariff(tariffCode: String): TariffSummary {
    val tariffDetails = when {
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

    if (tariffDetails == null) {
        throw IllegalArgumentException("$tariffCode not found in product $code")
    }

    val rates = tariffDetails.varying ?: tariffDetails.directDebitMonthly ?: throw TariffNotFoundException(tariffCode)

    return TariffSummary(
        productCode = code,
        fullName = fullName,
        displayName = displayName,
        description = description,
        availableFrom = availableFrom,
        availableTo = availableTo,
        tariffCode = rates.code,
        vatInclusiveUnitRate = rates.standardUnitRateIncVat?.roundToTwoDecimalPlaces() ?: throw IllegalArgumentException("unit rate not found for tariff $tariffCode"),
        vatInclusiveStandingCharge = rates.standingChargeIncVat.roundToTwoDecimalPlaces(),
    )
}

fun TariffDetailsDto.toTariffDetails(): TariffDetails? {
    val tariffPaymentTerm = when {
        directDebitMonthly != null -> TariffPaymentTerm.DIRECT_DEBIT_MONTHLY
        varying != null -> TariffPaymentTerm.VARYING
        else -> TariffPaymentTerm.UNKNOWN
    }

    val activePaymentTerm = when (tariffPaymentTerm) {
        TariffPaymentTerm.DIRECT_DEBIT_MONTHLY -> directDebitMonthly
        TariffPaymentTerm.VARYING -> varying
        else -> null
    }

    if (activePaymentTerm == null) return null

    return with(activePaymentTerm) {
        TariffDetails(
            tariffPaymentTerm = tariffPaymentTerm,
            tariffCode = code,
            vatInclusiveStandingCharge = standingChargeIncVat,
            vatInclusiveOnlineDiscount = onlineDiscountIncVat,
            vatInclusiveDualFuelDiscount = dualFuelDiscountIncVat,
            exitFeesType = ExitFeesType.fromApiValue(value = exitFeesType),
            vatInclusiveExitFees = exitFeesIncVat,
            vatInclusiveStandardUnitRate = standardUnitRateIncVat,
            vatInclusiveDayUnitRate = dayUnitRateIncVat,
            vatInclusiveNightUnitRate = nightUnitRateIncVat,
        )
    }
}
