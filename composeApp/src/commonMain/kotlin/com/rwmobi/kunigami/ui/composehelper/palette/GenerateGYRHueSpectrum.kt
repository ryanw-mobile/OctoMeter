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

package com.rwmobi.kunigami.ui.composehelper.palette

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.AppTheme

fun generateGYRHueSpectrum(
    saturation: Float = 0.5f,
    lightness: Float = 0.5f,
): List<Color> {
    val count = 100
    val startHue = 120f // Starting at green
    val endHue = 0f // Ending at red
    val delta = (endHue - startHue) / (count - 1) // Calculate delta for exactly 100 steps

    return List(count) { i ->
        val hue = startHue + delta * i // Compute the hue for this index
        Color.hsl(hue, saturation, lightness)
    }
}

@Preview
@Composable
private fun preview() {
    val colors = generateGYRHueSpectrum(
        saturation = 0.6f,
        lightness = 0.6f,
    )
    val gradient = Brush.horizontalGradient(colors)
    CommonPreviewSetup(
        modifier = Modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(AppTheme.dimens.grid_6)
                .height(AppTheme.dimens.grid_6)
                .fillMaxWidth()
                .background(brush = gradient),
        )
    }
}
