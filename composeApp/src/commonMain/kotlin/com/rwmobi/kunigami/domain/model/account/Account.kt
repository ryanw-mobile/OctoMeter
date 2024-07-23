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
    val postcode: String?,
    val movedInAt: Instant?,
    val movedOutAt: Instant?,
    val electricityMeterPoints: List<ElectricityMeterPoint>,
) {
    /**
     * Pick the active or last tariff code
     */
    fun getDefaultLatestTariffCode(): String? {
        return electricityMeterPoints.getOrNull(0)?.getLatestAgreement()?.tariffCode
    }

    fun getDefaultLatestTariffCode(mpan: String?): String? {
        if (mpan == null) return null

        return electricityMeterPoints.find {
            it.mpan == mpan
        }?.getLatestAgreement()?.tariffCode
    }

    fun getTariffHistory(mpan: String?): List<String> {
        if (mpan == null) return emptyList()

        return electricityMeterPoints.find {
            it.mpan == mpan
        }?.agreements?.map {
            it.tariffCode
        } ?: emptyList()
    }

    fun getDefaultMpan(): String? {
        return electricityMeterPoints.getOrNull(0)?.mpan
    }

    fun containsMpan(mpan: String?): Boolean {
        return electricityMeterPoints.any {
            it.mpan == mpan
        }
    }

    fun getElectricityMeterPoint(mpan: String): ElectricityMeterPoint? {
        return electricityMeterPoints.firstOrNull {
            it.mpan == mpan
        }
    }

    fun getElectricityMeterPoint(mpan: String, meterSerialNumber: String): ElectricityMeterPoint? {
        return electricityMeterPoints.firstOrNull {
            it.mpan == mpan && it.meterSerialNumbers.contains(meterSerialNumber)
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

    /***
     * Returns true when the account has at least one electricity meter.
     * Does not mean it is the meter user prefers
     */
    fun hasValidMeter(): Boolean {
        return electricityMeterPoints.isNotEmpty() &&
            electricityMeterPoints[0].meterSerialNumbers.isNotEmpty()
    }
}
