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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge_two_lines
import kunigami.composeapp.generated.resources.p_kwh
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

internal data class AgilePriceCardTextStyle(
    val standingChargeStyle: TextStyle,
    val agilePriceStyle: TextStyle,
    val agilePriceUnitStyle: TextStyle,
)

@Composable
internal fun AgilePriceCard(
    modifier: Modifier = Modifier,
    colorPalette: List<Color>,
    targetPercentage: Float,
    textStyle: AgilePriceCardTextStyle,
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
    agileTariff: Tariff?,
) {
    val dimension = LocalDensity.current.getDimension()

    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimension.grid_3),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val animatedVatInclusivePrice by animateFloatAsState(
                targetValue = vatInclusivePrice?.toFloat() ?: 0f,
            )
            if (vatInclusivePrice != null) {
                AgilePriceWithTrend(
                    modifier = Modifier.wrapContentSize(),
                    rateTrend = rateTrend,
                    colorPalette = colorPalette,
                    targetPercentage = targetPercentage,
                    textStyle = textStyle,
                    animatedVatInclusivePrice = animatedVatInclusivePrice,
                )
            }

            Spacer(modifier = Modifier.height(height = dimension.grid_1))

            agileTariff?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = textStyle.standingChargeStyle,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(
                        resource = Res.string.agile_tariff_standing_charge_two_lines,
                        it.vatInclusiveStandingCharge,
                    ),
                )
            }
        }
    }
}

@Composable
private fun AgilePriceWithTrend(
    modifier: Modifier = Modifier,
    targetPercentage: Float,
    animatedVatInclusivePrice: Float,
    colorPalette: List<Color>,
    textStyle: AgilePriceCardTextStyle,
    rateTrend: RateTrend? = null,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
    ) {
        rateTrend?.let {
            Icon(
                modifier = Modifier.size(size = dimension.grid_4),
                tint = colorPalette[(targetPercentage * colorPalette.lastIndex).toInt()],
                painter = painterResource(resource = it.drawableResource),
                contentDescription = it.name,
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

@Preview
@Composable
private fun AgilePriceWithTrendPreview() {
    CommonPreviewSetup {
        AgilePriceWithTrend(
            modifier = Modifier.wrapContentSize(),
            targetPercentage = 0.0f,
            animatedVatInclusivePrice = 0.0f,
            colorPalette = listOf(),
            rateTrend = null,
            textStyle = AgilePriceCardTextStyle(
                standingChargeStyle = MaterialTheme.typography.labelLarge,
                agilePriceStyle = MaterialTheme.typography.headlineLarge,
                agilePriceUnitStyle = MaterialTheme.typography.bodyLarge,
            ),
        )
    }
}
