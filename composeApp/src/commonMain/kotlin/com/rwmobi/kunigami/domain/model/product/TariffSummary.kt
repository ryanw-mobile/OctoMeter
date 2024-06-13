/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class TariffSummary(
    val productCode: String,
    val fullName: String,
    val displayName: String,
    val description: String,
    val tariffCode: String,
    val availableFrom: Instant,
    val availableTo: Instant?,
    val vatInclusiveUnitRate: Double,
    val vatInclusiveStandingCharge: Double,
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

        fun getRetailRegion(tariffCode: String): String? {
            return tariffCode.lastOrNull()?.let { lastCharacter ->
                if (('A'..'P').contains(lastCharacter)) {
                    lastCharacter.toString()
                } else {
                    null
                }
            }
        }

        fun isSingleRate(tariffCode: String): Boolean {
            return tariffCode.toCharArray().getOrNull(2)?.equals('1') == true
        }
    }

    fun extractProductCode(): String? = extractProductCode(tariffCode = tariffCode)
    fun getRetailRegion(): String? = getRetailRegion(tariffCode = tariffCode)
    fun isSingleRate(): Boolean = isSingleRate(tariffCode = tariffCode)
    fun isSameTariff(tariffCode: String?) = this.tariffCode == tariffCode
}
