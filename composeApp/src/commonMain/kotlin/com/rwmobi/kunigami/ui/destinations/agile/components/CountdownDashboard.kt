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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.utils.drawArcSegment
import com.rwmobi.kunigami.ui.utils.generateGYRHueColorPalette
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_expires_in
import kunigami.composeapp.generated.resources.agile_unit_rate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CountdownDashboard(
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

    Box(
        modifier = modifier.padding(all = dimension.grid_2)
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
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
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
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
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

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 24.dp)) {
            CountdownDashboard(
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
