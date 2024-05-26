/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.composehelper.drawArcSegment
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_expires_in
import kunigami.composeapp.generated.resources.agile_unit_rate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CountdownDashboardFullCircle(
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
    var columnWidth by remember { mutableStateOf(0) }
    val currentDensity = LocalDensity.current

    // Calculate font scale based on column width
    val fontScale = if (columnWidth > 0) {
        // Experimental: reduce font scale when the column width is small
        (columnWidth / 390f).coerceAtLeast(0.2f)
    } else {
        1f
    }

    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, fontScale = fontScale),
    ) {
        Box(
            modifier = modifier
                .padding(all = dimension.grid_1)
                .onSizeChanged { size ->
                    columnWidth = size.width
                }
                .drawBehind {
                    drawArcSegment(
                        colorPalette = colorPalette,
                        percentage = animatedPercentage,
                        strokeWidth = dimension.grid_1.toPx(),
                    )
                },
            propagateMinConstraints = true,
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.aspectRatio(ratio = 1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (vatInclusivePrice != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        letterSpacing = TextUnit(value = -1.5f, type = TextUnitType.Sp),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        text = vatInclusivePrice,
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(resource = Res.string.agile_unit_rate),
                    )

                    if (showCountdown) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = TextUnit(value = -1f, type = TextUnitType.Sp),
                            text = stringResource(
                                resource = Res.string.agile_expires_in,
                                expireMinutes,
                                expireSeconds.toString().padStart(length = 2, padChar = '0'),
                            ),
                        )
                    }

                    rateTrend?.let {
                        Icon(
                            modifier = Modifier
                                .padding(top = dimension.grid_0_5)
                                .size(size = dimension.grid_4),
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(resource = it.drawableResource),
                            contentDescription = it.name,
                        )
                    }
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
            CountdownDashboardFullCircle(
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
