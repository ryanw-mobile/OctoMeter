/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.runtime.Immutable

@Immutable
data class AgileUIEvent(
    val onRefresh: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onScrolledToTop: () -> Unit,
    val onNavigateToAccountTab: () -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
