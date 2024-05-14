/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.SingleProductApiResponse
import com.rwmobi.kunigami.domain.extensions.roundToNearestEvenHundredth
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

    val rates = tariffDetails.varying ?: tariffDetails.directDebitMonthly ?: throw IllegalArgumentException("rate not found for tariff $tariffCode")

    return Tariff(
        code = code,
        fullName = fullName,
        displayName = displayName,
        vatInclusiveUnitRate = rates.standardUnitRateIncVat?.roundToNearestEvenHundredth() ?: throw IllegalArgumentException("unit rate not found for tariff $tariffCode"),
        vatInclusiveStandingCharge = rates.standingChargeIncVat.roundToNearestEvenHundredth(),
    )
}
