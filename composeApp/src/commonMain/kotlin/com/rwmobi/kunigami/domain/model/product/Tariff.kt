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
    val tariffActiveAt: Instant,
    val vatInclusiveStandingCharge: Double,
    val vatInclusiveOnlineDiscount: Double,
    val vatInclusiveDualFuelDiscount: Double,
    val exitFeesType: ExitFeesType,
    val vatInclusiveExitFees: Double,
    val vatInclusiveStandardUnitRate: Double?,
    val vatInclusiveDayUnitRate: Double?,
    val vatInclusiveNightUnitRate: Double?,
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

        fun isSingleRate(tariffCode: String): Boolean {
            return tariffCode.toCharArray().getOrNull(2)?.equals('1') == true
        }

        fun isAgileProduct(tariffCode: String) = extractProductCode(tariffCode = tariffCode)?.contains("AGILE") == true
    }

    fun extractProductCode(): String? = extractProductCode(tariffCode = tariffCode)
    fun getRetailRegion(): RetailRegion? = getRetailRegion(tariffCode = tariffCode)
    fun isSingleRate(): Boolean = isSingleRate(tariffCode = tariffCode)
    fun isSameTariff(tariffCode: String?) = this.tariffCode == tariffCode
    fun hasStandardUnitRate(): Boolean = vatInclusiveStandardUnitRate != null
    fun hasDualRates(): Boolean = (
        vatInclusiveDayUnitRate != null &&
            vatInclusiveNightUnitRate != null
        )

    // TODO: WIP - We do not support dual rates and don't know how it works for now
    fun resolveUnitRate(referencePoint: Instant? = null): Double? {
        if (isSingleRate()) {
            return vatInclusiveStandardUnitRate
        }

        // Night: 00:30 - 07:30 UTC
        return referencePoint?.let { timestamp ->
            val utcTime = timestamp.toLocalDateTime(timeZone = TimeZone.UTC).time
            val nightRateStart = LocalTime(hour = 0, minute = 30)
            val nightRateEnd = LocalTime(hour = 7, minute = 30)
            if (utcTime >= nightRateStart && utcTime <= nightRateEnd) {
                vatInclusiveNightUnitRate
            } else {
                vatInclusiveDayUnitRate
            }
        }
    }

    fun isAgileProduct() = extractProductCode()?.contains("AGILE") == true
}
