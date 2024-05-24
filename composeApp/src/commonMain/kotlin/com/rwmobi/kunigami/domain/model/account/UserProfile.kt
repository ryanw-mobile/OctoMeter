/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import com.rwmobi.kunigami.domain.model.Tariff

data class UserProfile(
    val selectedMpan: String? = null,
    val selectedMeterSerialNumber: String? = null,
    val account: Account? = null,
    val tariff: Tariff? = null,
)
