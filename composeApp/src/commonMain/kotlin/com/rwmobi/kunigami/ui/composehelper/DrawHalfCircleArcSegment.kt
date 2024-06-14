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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.palette.generateGYRHueSpectrum
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.coin
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun DrawScope.drawHalfCircleArcSegment(
    percentage: Float,
    strokeWidth: Float? = null,
    colorPalette: List<Color>,
    startAngle: Float = 180f, // Start from the bottom left
    sweepAngle: Float = 180f, // Draw half circle
    darkenFactor: Float = 0.28f,
    iconPainter: Painter? = null,
    iconColorFilter: ColorFilter? = null,
    iconAlpha: Float? = null,
    onInnerSpaceMeasured: ((width: Float, height: Float) -> Unit)? = null,
) {
    onInnerSpaceMeasured?.let {
        it(size.width, size.height)
    }

    val dynamicStrokeWidth = strokeWidth ?: (size.width / 8f)
    val segmentAngle = sweepAngle / colorPalette.size
    val diameter = size.width - dynamicStrokeWidth
    val radius = diameter / 2
    val indicatorIndex = (percentage * colorPalette.size).toInt().coerceIn(colorPalette.indices)

    for (index in colorPalette.indices) {
        val isIndicator = index == indicatorIndex
        val effectiveStrokeWidth = if (isIndicator) dynamicStrokeWidth * 1.25f else dynamicStrokeWidth
        val effectiveDiameter = size.width - effectiveStrokeWidth
        val angle = startAngle + index * segmentAngle
        val isFilledSegment = index < (percentage * colorPalette.size).toInt()
        val color = if (isIndicator) {
            Color.Red
        } else if (isFilledSegment) {
            colorPalette[index]
        } else {
            colorPalette[index].darken(darkenFactor)
        }
        drawArc(
            color = color,
            startAngle = angle,
            sweepAngle = segmentAngle + 0.5f, // Make sure it leaves no gap
            useCenter = false,
            style = Stroke(width = effectiveStrokeWidth),
            size = Size(width = effectiveDiameter, height = effectiveDiameter),
            topLeft = Offset(
                x = effectiveStrokeWidth / 2,
                y = effectiveStrokeWidth / 2,
            ),
        )
    }

    // Draw divider line on top of the arc
    for (index in colorPalette.indices) {
        if (index < colorPalette.size - 1) {
            val isFilledSegment = index < (percentage * colorPalette.size).toInt()
            val backgroundColor = if (isFilledSegment) {
                colorPalette[index]
            } else {
                colorPalette[index].darken(darkenFactor)
            }
            val dividerAngle = startAngle + (index + 1) * segmentAngle
            val angleInRadians = toRadians(dividerAngle.toDouble())
            val startX = (radius - dynamicStrokeWidth / 2) * cos(angleInRadians).toFloat() + size.width / 2
            val startY = (radius - dynamicStrokeWidth / 2) * sin(angleInRadians).toFloat() + size.height
            val endX = (radius + dynamicStrokeWidth / 2) * cos(angleInRadians).toFloat() + size.width / 2
            val endY = (radius + dynamicStrokeWidth / 2) * sin(angleInRadians).toFloat() + size.height

            drawLine(
                color = backgroundColor.getContrastColor().copy(alpha = 0.5f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 0.5f,
            )
        }
    }

    iconPainter?.let {
        val innerRadius = size.height - dynamicStrokeWidth
        val sqrtFive = 2.236f
        val iconSize = innerRadius * 2 * sqrtFive / 5
        val iconX = (size.width - iconSize) / 2
        val iconY = dynamicStrokeWidth + (size.height - iconSize) / 4f
        with(iconPainter) {
            translate(
                left = iconX,
                top = iconY,
            ) {
                draw(
                    size = Size(iconSize, iconSize),
                    alpha = iconAlpha ?: 1.0f,
                    colorFilter = iconColorFilter,
                )
            }
        }
    }
}

private fun toRadians(deg: Double): Double = deg / 180.0 * PI

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            val iconPainter = painterResource(resource = Res.drawable.coin)
            val colorFilter = MaterialTheme.colorScheme.onSurface
            Box(
                modifier = Modifier
                    .width(512.dp)
                    .aspectRatio(2f) // Ensure the aspect ratio is 2:1
                    .drawBehind {
                        drawHalfCircleArcSegment(
                            percentage = 0.59f,
                            colorPalette = generateGYRHueSpectrum(),
                            iconPainter = iconPainter,
                            iconColorFilter = ColorFilter.tint(color = colorFilter),
                            iconAlpha = 0.32f,
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                // Extra contents
            }
        }
    }
}
