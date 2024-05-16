/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
sealed interface UsageScreenLayout {
    data object Portrait : UsageScreenLayout
    data class LandScape(val requestedMaxHeight: Dp) : UsageScreenLayout
}
