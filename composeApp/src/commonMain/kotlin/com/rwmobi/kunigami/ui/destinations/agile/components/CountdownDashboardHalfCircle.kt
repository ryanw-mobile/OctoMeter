/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.composehelper.drawHalfCircleArcSegment
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun CountdownDashboardHalfCircle(
    modifier: Modifier,
    colorPalette: List<Color>,
    animatedPercentage: Float,
    showCountdown: Boolean,
    expireMinutes: Long,
    expireSeconds: Long,
    vatInclusivePrice: String?,
    rateTrend: RateTrend?,
) {
    val dimension = LocalDensity.current.getDimension()

    val iconPainter = rateTrend?.drawableResource?.let { painterResource(resource = it) }
    val colorFilter = MaterialTheme.colorScheme.onBackground
    val dividerColor = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .width(256.dp)
            .aspectRatio(2f) // Ensure the aspect ratio is 2:1
            .drawBehind {
                drawHalfCircleArcSegment(
                    percentage = 0.6f,
                    colorPalette = generateGYRHueColorPalette(),
                    iconPainter = iconPainter,
                    iconColorFilter = ColorFilter.tint(color = colorFilter),
                )
            },
        contentAlignment = Alignment.Center,
    ) {}
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            CountdownDashboardHalfCircle(
                modifier = Modifier.aspectRatio(ratio = 1f),
                colorPalette = generateGYRHueColorPalette(
                    saturation = 0.6f,
                    lightness = 0.6f,
                ),
                animatedPercentage = 0.6f,
                showCountdown = true,
                expireMinutes = 15,
                expireSeconds = 10,
                vatInclusivePrice = "18.48",
                rateTrend = RateTrend.DOWN,
            )
        }
    }
}
