/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfile(
    val selectedMpan: String? = null,
    val selectedMeterSerialNumber: String? = null,
    val account: Account? = null,
) {
    fun getElectricityMeterPoint(): ElectricityMeterPoint? {
        return selectedMpan?.let { mpan ->
            account?.electricityMeterPoints?.firstOrNull { it.mpan == mpan }
        }
    }
}
