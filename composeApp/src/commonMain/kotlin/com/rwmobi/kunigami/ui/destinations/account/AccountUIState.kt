/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.model.ErrorMessage

data class AccountUIState(
    val isLoading: Boolean = true,
    val isDemoMode: Boolean = true,
    val selectedMpan: String? = null,
    val selectedMeterSerialNumber: String? = null,
    val account: Account? = null,
    val tariff: Tariff? = null,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
