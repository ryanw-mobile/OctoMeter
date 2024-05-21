/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.runtime.Immutable

@Immutable
data class AccountUIEvent(
    val onRefresh: () -> Unit,
    val onUpdateApiKeyClicked: () -> Unit,
    val onClearCredentialButtonClicked: () -> Unit,
    val onSubmitCredentials: (apiKey: String, accountNumber: String) -> Unit,
    val onMeterSerialNumberSelected: (mpan: String, meterSerialNumber: String) -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onScrolledToTop: () -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
