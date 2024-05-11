/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

data class TariffsUIEvent(
    val onRefresh: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
