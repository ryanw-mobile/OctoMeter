/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.min

internal fun DrawScope.drawArcSegment(
    percentage: Float,
    strokeWidth: Float,
    startAngle: Float = 135f,
    sweepAngle: Float = 270f,
    darkenFactor: Float = 0.28f,
    colorPalette: List<Color>,
) {
    val radius = size.minDimension / 2
    val segmentAngle = 270f / colorPalette.size

    for (i in colorPalette.indices.reversed()) {
        val angle = startAngle + i * segmentAngle
        val isFilledSegment = i < (percentage * colorPalette.size).toInt()
        val color = if (isFilledSegment) {
            colorPalette[i]
        } else {
            colorPalette[i].darken(darkenFactor)
        }

        drawArc(
            color = color,
            startAngle = angle,
            sweepAngle = min(segmentAngle, sweepAngle - (angle - startAngle)),
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = size,
            topLeft = Offset(
                x = (size.width - 2 * radius) / 2,
                y = (size.height - 2 * radius) / 2,
            ),
        )
    }
}
