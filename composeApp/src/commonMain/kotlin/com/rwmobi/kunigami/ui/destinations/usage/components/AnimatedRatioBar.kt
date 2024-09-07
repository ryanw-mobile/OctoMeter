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

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.unit_percent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AnimatedRatioBar(
    modifier: Modifier = Modifier,
    consumptionRatio: Double, // Ratio between 0.0 and 1.0
) {
    val consumptionColor = MaterialTheme.colorScheme.primary
    val standingChargeColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    var targetRatio by remember { mutableStateOf(0f) }
    // Trigger the animation when the composable is first composed
    LaunchedEffect(consumptionRatio) {
        targetRatio = consumptionRatio.coerceIn(minimumValue = 0.0, maximumValue = 1.0).toFloat()
    }
    val animatedRatio by animateFloatAsState(
        targetValue = targetRatio,
        animationSpec = tween(durationMillis = 1_000),
    )

    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .drawBehind {
                val consumptionWidth = size.width * animatedRatio
                val standingChargeWidth = size.width - consumptionWidth

                drawIntoCanvas {
                    // Draw the consumption part
                    drawRect(
                        color = consumptionColor,
                        size = size.copy(width = consumptionWidth),
                    )

                    // Draw the standing charge part
                    drawRect(
                        color = standingChargeColor,
                        topLeft = androidx.compose.ui.geometry.Offset(x = consumptionWidth, y = 0f),
                        size = size.copy(width = standingChargeWidth),
                    )
                }
            },
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            text = stringResource(resource = Res.string.unit_percent, (animatedRatio * 100).toString(precision = 0)),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup(
        modifier = Modifier.padding(all = 8.dp),
    ) { dimension ->
        AnimatedRatioBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimension.grid_2),
            consumptionRatio = 0.64,
        )
    }
}
