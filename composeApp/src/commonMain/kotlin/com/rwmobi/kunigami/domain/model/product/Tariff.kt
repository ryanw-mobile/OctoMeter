/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.ui.model.product.RetailRegion
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
data class Tariff(
    // Product data
    val productCode: String,
    val fullName: String,
    val displayName: String,
    val description: String,
    val isVariable: Boolean,
    val availability: ClosedRange<Instant>,

    val tariffPaymentTerm: TariffPaymentTerm,
    // The layered DTOs were flattened
    val tariffCode: String,
    val vatInclusiveStandingCharge: Double,
    val exitFeesType: ExitFeesType,
    val vatInclusiveExitFees: Double,
    val vatInclusiveStandardUnitRate: Double?,
    val vatInclusiveDayUnitRate: Double?,
    val vatInclusiveNightUnitRate: Double?,
    val vatInclusiveOffPeakRate: Double?,
) {
    companion object {
        fun extractProductCode(tariffCode: String): String? {
            val parts = tariffCode.split("-")
            // Check if there are enough parts to remove
            if (parts.size > 3) {
                // Exclude the first two and the last segments
                val relevantParts = parts.drop(2).dropLast(1)
                return relevantParts.joinToString("-")
            }
            return null
        }

        fun getRetailRegion(tariffCode: String): RetailRegion? {
            val regionCode = tariffCode.lastOrNull()?.let { lastCharacter ->
                if (('A'..'P').contains(lastCharacter)) {
                    lastCharacter.toString()
                } else {
                    null
                }
            }

            return RetailRegion.fromCode(regionCode)
        }

        fun isSingleFuel(tariffCode: String): Boolean {
            return tariffCode.toCharArray().getOrNull(2)?.equals('1') == true
        }

        fun isAgileProduct(tariffCode: String) = extractProductCode(tariffCode = tariffCode)?.contains("AGILE") == true
    }

    fun extractProductCode(): String? = extractProductCode(tariffCode = tariffCode)
    fun getRetailRegion(): RetailRegion? = getRetailRegion(tariffCode = tariffCode)
    fun isSingleFuel(): Boolean = isSingleFuel(tariffCode = tariffCode)
    fun isSameTariff(tariffCode: String?) = this.tariffCode == tariffCode

    fun getElectricityTariffType(): ElectricityTariffType {
        return when {
            vatInclusiveOffPeakRate != null && vatInclusiveDayUnitRate != null && vatInclusiveNightUnitRate != null -> ElectricityTariffType.THREE_RATE
            vatInclusiveDayUnitRate != null && vatInclusiveNightUnitRate != null -> ElectricityTariffType.DAY_NIGHT
            vatInclusiveStandardUnitRate != null -> ElectricityTariffType.STANDARD
            else -> ElectricityTariffType.UNKNOWN
        }
    }

    // TODO: WIP - We do not support dual rates and don't know how it works for now
    fun resolveUnitRate(referencePoint: Instant? = null): Double? {
        return when (getElectricityTariffType()) {
            ElectricityTariffType.STANDARD -> {
                vatInclusiveStandardUnitRate
            }

            ElectricityTariffType.DAY_NIGHT -> {
                // TODO: Each tariff may have different time range
                // Night: 00:30 - 07:30 UTC
                referencePoint?.let { timestamp ->
                    val utcTime = timestamp.toLocalDateTime(timeZone = TimeZone.UTC).time
                    val nightRateStart = LocalTime(hour = 0, minute = 30)
                    val nightRateEnd = LocalTime(hour = 7, minute = 30)
                    if (utcTime in nightRateStart..nightRateEnd) {
                        vatInclusiveNightUnitRate
                    } else {
                        vatInclusiveDayUnitRate
                    }
                }
            }

            ElectricityTariffType.THREE_RATE -> {
                // TODO: Each tariff may have different time range
                // Peak: 16:00 - 19:00
                // Night: 00:30 - 07:30 UTC
                referencePoint?.let { timestamp ->
                    val localtime = timestamp.toLocalDateTime(timeZone = TimeZone.of("Europe/London")).time
                    val peakRateStart = LocalTime(hour = 16, minute = 0)
                    val peakRateEnd = LocalTime(hour = 19, minute = 0)
                    val nightRateStart = LocalTime(hour = 2, minute = 0)
                    val nightRateEnd = LocalTime(hour = 5, minute = 0)
                    when (localtime) {
                        in nightRateStart..nightRateEnd -> {
                            vatInclusiveNightUnitRate
                        }

                        in peakRateStart..peakRateEnd -> {
                            vatInclusiveDayUnitRate
                        }

                        else -> {
                            vatInclusiveOffPeakRate
                        }
                    }
                }
            }

            else -> null
        }
    }

    fun isAgileProduct() = extractProductCode()?.contains("AGILE") == true
}
