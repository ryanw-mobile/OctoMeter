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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.palette.generateGYRHueSpectrum
import kotlin.math.min

internal fun DrawScope.drawArcSegment(
    percentage: Float,
    strokeWidth: Float,
    startAngle: Float = 135f,
    sweepAngle: Float = 270f,
    darkenFactor: Float = 0.28f,
    colorPalette: List<Color>,
) {
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
            size = Size(width = size.width - strokeWidth, height = size.height - strokeWidth),
            topLeft = Offset(
                x = strokeWidth / 2,
                y = strokeWidth - (strokeWidth / 2),
            ),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            Box(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .width(240.dp)
                    .aspectRatio(1f) // Ensure the aspect ratio is 2:1
                    .drawBehind {
                        drawArcSegment(
                            percentage = 0.6f,
                            colorPalette = generateGYRHueSpectrum(),
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
