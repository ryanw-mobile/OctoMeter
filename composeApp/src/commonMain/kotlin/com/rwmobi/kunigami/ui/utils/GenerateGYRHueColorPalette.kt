/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import androidx.compose.ui.graphics.Color

fun generateGYRHueColorPalette(
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
