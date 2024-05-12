/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model

import androidx.compose.ui.unit.Dp

data class ScreenSizeInfo(
    val heightPx: Int,
    val widthPx: Int,
    val heightDp: Dp,
    val widthDp: Dp,
) {
    fun isPortrait() = widthPx < heightPx
}
