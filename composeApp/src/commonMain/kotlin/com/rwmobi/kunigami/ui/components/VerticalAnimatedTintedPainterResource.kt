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

package com.rwmobi.kunigami.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun VerticalAnimatedTintedPainterResource(
    modifier: Modifier = Modifier,
    painter: Painter,
    color: Color,
    durationMillis: Int,
) {
    val transition = rememberInfiniteTransition()
    val clipProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = FastOutLinearInEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
    )

    Canvas(modifier = modifier) {
        val clipHeight = size.height * clipProgress
        clipRect(
            top = 0f,
            bottom = clipHeight,
            left = 0f,
            right = size.width,
        ) {
            with(painter) {
                draw(
                    size = size,
                    colorFilter = ColorFilter.tint(color = color),
                )
            }
        }
    }
}
