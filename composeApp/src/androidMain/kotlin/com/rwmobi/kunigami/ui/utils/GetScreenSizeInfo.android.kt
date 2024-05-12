/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo

@Composable
actual fun getScreenSizeInfo(): ScreenSizeInfo {
    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val screenSizeInfo = remember(density, config) {
        val heightDp = config.screenHeightDp.dp
        val widthDp = config.screenWidthDp.dp
        ScreenSizeInfo(
            heightPx = with(density) { heightDp.roundToPx() },
            widthPx = with(density) { widthDp.roundToPx() },
            heightDp = heightDp,
            widthDp = widthDp,
        ) as ScreenSizeInfo
    }
    return screenSizeInfo
}
