/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class AccountUIEvent(
    val onRefresh: () -> Unit,
    val onClearCredentialButtonClicked: () -> Unit,
    val onSubmitCredentials: (apiKey: String, accountNumber: String, stringResolver: suspend (resId: StringResource) -> String) -> Unit,
    val onMeterSerialNumberSelected: (mpan: String, meterSerialNumber: String) -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onSpecialErrorScreenShown: () -> Unit,
    val onClearCache: (stringResolver: suspend (resId: StringResource) -> String) -> Unit,
    val onScrolledToTop: () -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
