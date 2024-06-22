/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge_two_lines
import org.jetbrains.compose.resources.stringResource

internal data class AgilePriceCardTextStyle(
    val standingChargeStyle: TextStyle,
    val agilePriceStyle: TextStyle,
    val agilePriceUnitStyle: TextStyle,
)

@Composable
internal fun AgilePriceCard(
    modifier: Modifier = Modifier,
    textStyle: AgilePriceCardTextStyle,
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
    rateTrendIconTint: Color? = null,
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
                    rateTrendIconTint = rateTrendIconTint,
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
