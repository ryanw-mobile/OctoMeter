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

package com.rwmobi.kunigami.ui.extensions

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.model.PlatformType

actual fun WindowSizeClass.getPlatformType() = PlatformType.ANDROID

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
actual fun calculateWindowSizeClass(): WindowSizeClass {
    val activity = LocalActivity.current // This can be null

    // If in preview mode or activity is null, provide a default WindowSizeClass.
    // You might want to make this default configurable or based on common device types for previews.
    if (LocalInspectionMode.current || activity == null) {
        // Fallback using LocalWindowInfo for previews or when activity is null
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current

        // containerSize is in pixels, convert to Dp for WindowSizeClass.calculateFromSize
        val containerWidthPx = windowInfo.containerSize.width
        val containerHeightPx = windowInfo.containerSize.height

        if (containerWidthPx == 0 || containerHeightPx == 0) {
            // Fallback to a very common default if container size is not yet available
            // This might happen in very early composition stages or specific preview setups.
            return WindowSizeClass.calculateFromSize(DpSize(600.dp, 800.dp))
        }

        val containerWidthDp = with(density) { containerWidthPx.toDp() }
        val containerHeightDp = with(density) { containerHeightPx.toDp() }

        return WindowSizeClass.calculateFromSize(
            size = DpSize(
                width = containerWidthDp,
                height = containerHeightDp,
            ),
        )
    }

    // When an activity is available, use the official API
    return androidx.compose.material3.windowsizeclass.calculateWindowSizeClass(activity)
}
