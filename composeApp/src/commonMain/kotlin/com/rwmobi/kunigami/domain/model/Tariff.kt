/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Tariff(
    val code: String,
    val fullName: String,
    val displayName: String,
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

    fun extractProductCode(): String? = Companion.extractProductCode(tariffCode = code)
    fun getRetailRegion(): String? = Companion.getRetailRegion(tariffCode = code)
    fun isSingleRate(): Boolean = Companion.isSingleRate(tariffCode = code)
}
