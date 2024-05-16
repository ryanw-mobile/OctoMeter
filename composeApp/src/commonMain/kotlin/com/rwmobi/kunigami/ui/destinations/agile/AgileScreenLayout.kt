/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
sealed interface AgileScreenLayout {
    data object Portrait : AgileScreenLayout
    data class LandScape(val requestedMaxHeight: Dp) : AgileScreenLayout
}
