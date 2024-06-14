/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
    ) { dimension ->
        Box(
            modifier = Modifier
                .padding(dimension.grid_6)
                .height(dimension.grid_6)
                .fillMaxWidth()
                .background(brush = gradient),
        )
    }
}
