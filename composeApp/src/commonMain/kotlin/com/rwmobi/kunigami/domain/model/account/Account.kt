/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class Account(
    val id: Int,
    val accountNumber: String,
    val fullAddress: String?,
    val movedInAt: Instant?,
    val movedOutAt: Instant?,
    val electricityMeterPoints: List<ElectricityMeterPoint>,
) {
    /**
     * Pick the first tariff we can find from the data we keep.
     */
    fun getDefaultTariffCode(): String? {
        return electricityMeterPoints.getOrNull(0)?.currentAgreement?.tariffCode
    }

    fun getTariffCode(mpan: String?): String? {
        if (mpan == null) return null

        return electricityMeterPoints.find {
            it.mpan == mpan
        }?.currentAgreement?.tariffCode
    }

    fun getDefaultMpan(): String? {
        return electricityMeterPoints.getOrNull(0)?.mpan
    }

    fun containsMpan(mpan: String?): Boolean {
        return electricityMeterPoints.any {
            it.mpan == mpan
        }
    }

    fun getDefaultMeterSerialNumber(): String? {
        return electricityMeterPoints.getOrNull(0)?.meterSerialNumbers?.getOrNull(0)
    }

    fun containsMeterSerialNumber(mpan: String?, serial: String?): Boolean {
        if (mpan == null || serial == null) {
            return false
        }

        return electricityMeterPoints.firstOrNull {
            it.mpan == mpan
        }?.meterSerialNumbers?.contains(serial) == true
    }
}
