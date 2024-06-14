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
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.coin
import org.jetbrains.compose.resources.painterResource

internal fun DrawScope.drawPlainColorArc(
    strokeWidth: Float? = null,
    color: Color,
    startAngle: Float = 180f, // Start from the bottom left
    sweepAngle: Float = 180f, // Draw half circle
    iconPainter: Painter? = null,
    iconColorFilter: ColorFilter? = null,
    iconAlpha: Float? = null,
    onInnerSpaceMeasured: ((width: Float, height: Float) -> Unit)? = null,
) {
    onInnerSpaceMeasured?.let {
        it(size.width, size.height)
    }

    val dynamicStrokeWidth = strokeWidth ?: (size.width / 8f)

    val effectiveDiameter = size.width - dynamicStrokeWidth

    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = dynamicStrokeWidth),
        size = Size(width = effectiveDiameter, height = effectiveDiameter),
        topLeft = Offset(
            x = dynamicStrokeWidth / 2,
            y = dynamicStrokeWidth / 2,
        ),
    )

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
                        drawPlainColorArc(
                            color = Color.Red,
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
