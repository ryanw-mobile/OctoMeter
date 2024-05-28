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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.composehelper.drawHalfCircleArcSegment
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_expire
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DashboardWidget(
    modifier: Modifier,
    percentage: Float,
    countDownText: String?,
    colorPalette: List<Color>,
) {
    val countDownTextMagicWidth = 432f
    var textBoundWidth by remember { mutableStateOf(countDownTextMagicWidth) }
    val fontScale = remember(textBoundWidth) { textBoundWidth / countDownTextMagicWidth }

    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )
    Box(
        modifier = modifier
            .aspectRatio(
                ratio = 2f,
                matchHeightConstraintsFirst = true,
            ) // Ensure the aspect ratio is 2:1
            .drawBehind {
                drawHalfCircleArcSegment(
                    percentage = animatedPercentage,
                    colorPalette = colorPalette,
                    onInnerSpaceMeasured = { width, _ ->
                        textBoundWidth = width
                    },
                )
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        countDownText?.let {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = fontScale,
                ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        text = stringResource(resource = Res.string.agile_expire),
                    )
                    Text(
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        text = it,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            DashboardWidget(
                modifier = Modifier.aspectRatio(ratio = 2f),
                colorPalette = generateGYRHueColorPalette(),
                countDownText = "33:33",
                percentage = 0.8f,
            )
        }
    }
}
