/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AccountScreenLayoutStyle {
    data object Compact : AccountScreenLayoutStyle
    data object Wide : AccountScreenLayoutStyle
    data object WideWrapped : AccountScreenLayoutStyle
}
