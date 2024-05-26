/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.composehelper.drawHalfCircleArcSegment
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
internal fun DashboardWidget(
    modifier: Modifier,
    percentage: Float,
    colorPalette: List<Color>,
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )
    Box(
        modifier = modifier
            .aspectRatio(2f) // Ensure the aspect ratio is 2:1
            .drawBehind {
                drawHalfCircleArcSegment(
                    percentage = animatedPercentage,
                    colorPalette = colorPalette,
                )
            },
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            DashboardWidget(
                modifier = Modifier.aspectRatio(ratio = 1f),
                colorPalette = generateGYRHueColorPalette(),
                percentage = 0.8f,
            )
        }
    }
}
