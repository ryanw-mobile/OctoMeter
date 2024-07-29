/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastAny
import kotlinx.datetime.Instant

@Immutable
data class Account(
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
            it.mpan == mpan && it.meters.fastAny { meter ->
                meter.serialNumber == meterSerialNumber
            }
        }
    }

    fun getDefaultMeterSerialNumber(): String? {
        return electricityMeterPoints.getOrNull(0)
            ?.meters?.getOrNull(0)?.serialNumber
    }

    fun containsMeterSerialNumber(mpan: String?, meterSerialNumber: String?): Boolean {
        if (mpan == null || meterSerialNumber == null) {
            return false
        }

        return electricityMeterPoints.firstOrNull {
            it.mpan == mpan && it.meters.fastAny { meter ->
                meter.serialNumber == meterSerialNumber
            }
        } != null
    }

    /***
     * Returns true when the account has at least one electricity meter.
     * Does not mean it is the meter user prefers
     */
    fun hasValidMeter(): Boolean {
        return electricityMeterPoints.isNotEmpty() &&
            electricityMeterPoints[0].meters.isNotEmpty()
    }
}
