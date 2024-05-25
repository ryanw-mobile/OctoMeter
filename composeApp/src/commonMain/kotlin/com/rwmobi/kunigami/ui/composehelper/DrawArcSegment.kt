/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.composehelper

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
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

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            Box(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .width(240.dp)
                    .aspectRatio(1f) // Ensure the aspect ratio is 2:1
                    .drawBehind {
                        drawArcSegment(
                            percentage = 0.6f,
                            colorPalette = generateGYRHueColorPalette(),
                            strokeWidth = 24f,
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Display something")
            }
        }
    }
}
