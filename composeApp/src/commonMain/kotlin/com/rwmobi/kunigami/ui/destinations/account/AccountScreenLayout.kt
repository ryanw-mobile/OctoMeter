/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

sealed interface AccountScreenLayout {
    data object Compact : AccountScreenLayout
    data object Wide : AccountScreenLayout
    data object ConstraintedWide : AccountScreenLayout
}
