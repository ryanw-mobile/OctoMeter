/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TariffScreenLayout {
    data class Compact(val useBottomSheet: Boolean) : TariffScreenLayout
    data class Wide(val useBottomSheet: Boolean) : TariffScreenLayout
    data object ListDetailPane : TariffScreenLayout
}
