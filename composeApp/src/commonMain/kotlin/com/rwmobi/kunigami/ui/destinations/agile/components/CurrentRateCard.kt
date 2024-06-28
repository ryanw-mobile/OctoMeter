/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.WidgetCard
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_current_rate
import kunigami.composeapp.generated.resources.p_kwh
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

internal data class CurrentRateCardTextStyle(
    val standingChargeStyle: TextStyle,
    val agilePriceStyle: TextStyle,
    val agilePriceUnitStyle: TextStyle,
)

@Composable
internal fun CurrentRateCard(
    modifier: Modifier = Modifier,
    textStyle: CurrentRateCardTextStyle,
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
    rateTrendIconTint: Color? = null,
) {
    val dimension = LocalDensity.current.getDimension()

    WidgetCard(
        modifier = modifier,
        heading = stringResource(resource = Res.string.agile_current_rate).uppercase(),
        contents = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val animatedVatInclusivePrice by animateFloatAsState(
                    targetValue = vatInclusivePrice?.toFloat() ?: 0f,
                )
                if (vatInclusivePrice != null) {
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
                    ) {
                        if (rateTrend != null && rateTrendIconTint != null) {
                            Icon(
                                modifier = Modifier.size(size = dimension.grid_4),
                                tint = rateTrendIconTint,
                                painter = painterResource(resource = rateTrend.drawableResource),
                                contentDescription = rateTrend.name,
                            )
                        }

                        Text(
                            modifier = Modifier.wrapContentSize(),
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            style = textStyle.agilePriceStyle,
                            fontWeight = FontWeight.Bold,
                            text = animatedVatInclusivePrice.toString(precision = 2),
                        )

                        Text(
                            modifier = Modifier.wrapContentSize(),
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            style = textStyle.agilePriceUnitStyle,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(resource = Res.string.p_kwh),
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        CurrentRateCard(
            modifier = Modifier.wrapContentSize(),
            rateTrendIconTint = Color.Red,
            rateTrend = RateTrend.DOWN,
            vatInclusivePrice = 25.08,
            textStyle = CurrentRateCardTextStyle(
                standingChargeStyle = MaterialTheme.typography.labelLarge,
                agilePriceStyle = MaterialTheme.typography.headlineLarge,
                agilePriceUnitStyle = MaterialTheme.typography.bodyLarge,
            ),
        )
    }
}
