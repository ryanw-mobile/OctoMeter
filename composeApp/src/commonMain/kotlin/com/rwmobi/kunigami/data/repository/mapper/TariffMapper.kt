/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.domain.exceptions.TariffNotFoundException
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.Tariff

fun SingleProductApiResponse.toTariff(tariffCode: String): Tariff {
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

    return Tariff(
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
