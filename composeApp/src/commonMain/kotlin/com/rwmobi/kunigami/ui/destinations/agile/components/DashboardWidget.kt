/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.drawHalfCircleArcSegment
import com.rwmobi.kunigami.ui.composehelper.drawPlainColorArc
import com.rwmobi.kunigami.ui.composehelper.palette.RatePalette
import com.rwmobi.kunigami.ui.composehelper.palette.generateGYRHueSpectrum
import com.rwmobi.kunigami.ui.composehelper.shouldUseDarkTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_expire
import org.jetbrains.compose.resources.stringResource

/***
 * percentage: can be negative
 */
@Composable
internal fun DashboardWidget(
    modifier: Modifier,
    percentage: Float,
    countDownText: String?,
    colorPalette: List<Color>,
) {
    val countDownTextMagicWidth = 480f
    var textBoundWidth by remember { mutableStateOf(countDownTextMagicWidth) }
    val fontScale = remember(textBoundWidth) { textBoundWidth / countDownTextMagicWidth }

    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1_000, easing = FastOutSlowInEasing),
    )

    var breathingColor by remember { mutableStateOf(Color.Transparent) }
    var isAnimatedPercentageComplete by remember { mutableStateOf(false) }
    val shouldUseDarkTheme = shouldUseDarkTheme()

    LaunchedEffect(animatedPercentage) {
        isAnimatedPercentageComplete = animatedPercentage == percentage
    }

    if (isAnimatedPercentageComplete && animatedPercentage <= 0) {
        val infiniteTransition = rememberInfiniteTransition()
        val breathingPercentage by infiniteTransition.animateFloat(
            initialValue = animatedPercentage,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2_000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        )
        breathingColor = RatePalette.lookupColorFromPercentage(
            percentage = breathingPercentage,
            shouldUseDarkTheme = shouldUseDarkTheme,
        )
    }

    Box(
        modifier = modifier
            .aspectRatio(
                ratio = 2f,
                matchHeightConstraintsFirst = true,
            ) // Ensure the aspect ratio is 2:1
            .drawBehind {
                if (animatedPercentage >= 0) {
                    drawHalfCircleArcSegment(
                        percentage = animatedPercentage,
                        colorPalette = colorPalette,
                        onInnerSpaceMeasured = { width, _ ->
                            textBoundWidth = width
                        },
                    )
                } else {
                    drawPlainColorArc(
                        color = if (isAnimatedPercentageComplete) {
                            breathingColor
                        } else {
                            RatePalette.lookupColorFromPercentage(
                                percentage = animatedPercentage,
                                shouldUseDarkTheme = shouldUseDarkTheme,
                            )
                        },
                        onInnerSpaceMeasured = { width, _ ->
                            textBoundWidth = width
                        },
                    )
                }
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        countDownText?.let {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = 2.625f,
                    fontScale = fontScale,
                ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Bottom,
                                trim = LineHeightStyle.Trim.Both,
                            ),
                        ),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        text = stringResource(resource = Res.string.agile_expire),
                    )
                    Text(
                        style = MaterialTheme.typography.headlineMedium.copy(
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Bottom,
                                trim = LineHeightStyle.Trim.Both,
                            ),
                        ),
                        maxLines = 1,
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
    CommonPreviewSetup {
        DashboardWidget(
            modifier = Modifier.aspectRatio(ratio = 2f),
            colorPalette = generateGYRHueSpectrum(),
            countDownText = "33:33",
            percentage = 0.8f,
        )
    }
}
