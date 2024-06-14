/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.composehelper

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

fun generateFreezingBlueColorPalette(
    saturation: Float = 0.6f,
    lightness: Float = 0.6f,
): List<Color> {
    val count = 100
    val startHue = 180f // Starting at cyan
    val endHue = 240f // Ending at blue
    val delta = (endHue - startHue) / (count - 1) // Calculate delta for exactly 100 steps

    return List(count) { i ->
        val hue = startHue + delta * i // Compute the hue for this index
        val adjustedLightness = if (lightness < 0.5f) {
            lightness + (i.toFloat() / count) * 0.3f // Gradually increase lightness
        } else {
            lightness - (i.toFloat() / count) * 0.3f // Gradually decrease lightness
        }
        Color.hsl(hue, saturation, adjustedLightness)
    }
}

@Preview
@Composable
private fun Preview() {
    val colors = generateFreezingBlueColorPalette(
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
