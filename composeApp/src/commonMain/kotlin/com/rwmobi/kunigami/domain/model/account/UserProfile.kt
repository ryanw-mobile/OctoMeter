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
    val selectedMpan: String,
    val selectedMeterSerialNumber: String,
    val account: Account,
) {
    fun getSelectedElectricityMeterPoint(): ElectricityMeterPoint? {
        return account.electricityMeterPoints.firstOrNull { it.mpan == selectedMpan }
    }
}
