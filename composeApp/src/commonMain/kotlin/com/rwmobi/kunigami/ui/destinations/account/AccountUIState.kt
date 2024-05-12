/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.ui.model.ErrorMessage

data class AccountUIState(
    val hasApiKey: Boolean = false,
    val selectedMpan: String? = null,
    val selectedMeterSerialNumber: String? = null,
    val account: Account? = null,
    val isLoading: Boolean = true,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
