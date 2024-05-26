/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.chart

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
sealed interface RequestedChartLayout {
    data object Portrait : RequestedChartLayout
    data class LandScape(val requestedMaxHeight: Dp) : RequestedChartLayout
}
