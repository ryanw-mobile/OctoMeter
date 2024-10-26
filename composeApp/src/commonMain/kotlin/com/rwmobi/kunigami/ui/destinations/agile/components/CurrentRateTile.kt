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

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.HorizontalAnimatedTintedPainterResource
import com.rwmobi.kunigami.ui.components.VerticalAnimatedTintedPainterResource
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kotlinx.coroutines.delay
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_current_rate
import kunigami.composeapp.generated.resources.overpriced
import kunigami.composeapp.generated.resources.p_kwh
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CurrentRateTile(
    modifier: Modifier = Modifier,
    isCurrentRateOverpriced: Boolean,
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
    rateTrendIconTint: Color? = null,
) {
    val dimension = getScreenSizeInfo().getDimension()
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(dimension.grid_2),
        verticalArrangement = Arrangement.Top,
    ) {
        val animatedVatInclusivePrice by animateFloatAsState(
            targetValue = vatInclusivePrice?.toFloat() ?: 0f,
        )
        if (vatInclusivePrice != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.displayMedium,
                    text = animatedVatInclusivePrice.toString(precision = 2),
                )

                if (rateTrend != null && rateTrendIconTint != null) {
                    val rateTrendModifier = Modifier
                        .size(size = dimension.grid_4)
                        .semantics { contentDescription = rateTrend.name }

                    when (rateTrend) {
                        RateTrend.UP, RateTrend.DOWN -> {
                            HorizontalAnimatedTintedPainterResource(
                                modifier = rateTrendModifier,
                                color = rateTrendIconTint,
                                painter = painterResource(resource = rateTrend.drawableResource),
                                durationMillis = 2_000,
                            )
                        }

                        RateTrend.STEADY -> {
                            VerticalAnimatedTintedPainterResource(
                                modifier = rateTrendModifier,
                                color = rateTrendIconTint,
                                painter = painterResource(resource = rateTrend.drawableResource),
                                durationMillis = 2_000,
                            )
                        }
                    }
                }
            }
        }

        var isOverpricedTagVisible by remember { mutableStateOf(isCurrentRateOverpriced) }
        LaunchedEffect(isCurrentRateOverpriced) {
            if (isCurrentRateOverpriced) {
                while (true) {
                    isOverpricedTagVisible = !isOverpricedTagVisible
                    delay(500)
                }
            } else {
                // Clean up if we move on to a cheaper slot
                isOverpricedTagVisible = false
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimension.grid_1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                text = stringResource(resource = Res.string.p_kwh),
            )

            AnimatedVisibility(
                visible = isOverpricedTagVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(horizontal = dimension.grid_1),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(resource = Res.string.overpriced).uppercase(),
                )
            }
        }

        Spacer(modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(resource = Res.string.agile_current_rate),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        CurrentRateTile(
            modifier = Modifier
                .width(dimension.widgetWidthFull)
                .height(dimension.widgetHeight),
            isCurrentRateOverpriced = true,
            rateTrendIconTint = Color.Red,
            rateTrend = RateTrend.DOWN,
            vatInclusivePrice = 25.08,
        )
    }
}
