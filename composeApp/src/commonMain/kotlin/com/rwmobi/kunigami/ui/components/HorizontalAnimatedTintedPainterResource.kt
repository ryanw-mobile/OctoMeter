/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
fun HorizontalAnimatedTintedPainterResource(
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
        val clipWidth = size.width * clipProgress
        clipRect(
            top = 0f,
            bottom = size.height,
            left = 0f,
            right = clipWidth,
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
