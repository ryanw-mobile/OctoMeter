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

fun generateFreezingBlueSpectrum(
    initialSaturation: Float = 0.4f, // Starting with a moderate saturation
    finalSaturation: Float = 0.7f, // Ending with a higher saturation
    initialLightness: Float = 0.85f, // Starting with a very light color
    finalLightness: Float = 0.6f, // Ending with a moderate color
): List<Color> {
    val count = 100
    val startHue = 180f // Starting at cyan
    val endHue = 220f // Ending at a colder blue
    val deltaHue = (endHue - startHue) / (count - 1) // Calculate delta for exactly 100 steps
    val deltaSaturation = (finalSaturation - initialSaturation) / (count - 1) // Calculate delta for saturation
    val deltaLightness = (finalLightness - initialLightness) / (count - 1) // Calculate delta for lightness

    return List(count) { i ->
        val hue = startHue + deltaHue * i // Compute the hue for this index
        val saturation = initialSaturation + deltaSaturation * i // Gradually increase saturation
        val lightness = initialLightness + deltaLightness * i // Gradually decrease lightness
        Color.hsl(hue, saturation, lightness)
    }
}

@Preview
@Composable
private fun Preview() {
    val colors = generateFreezingBlueSpectrum()
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
