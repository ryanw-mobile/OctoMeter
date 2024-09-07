/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.composehelper

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
