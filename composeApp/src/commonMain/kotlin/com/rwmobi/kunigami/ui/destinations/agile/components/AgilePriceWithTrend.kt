/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.p_kwh
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AgilePriceWithTrend(
    modifier: Modifier = Modifier,
    animatedVatInclusivePrice: Float,
    textStyle: AgilePriceCardTextStyle,
    rateTrend: RateTrend? = null,
    rateTrendIconTint: Color? = null,
) {
    val dimension = LocalDensity.current.getDimension()

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

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        AgilePriceWithTrend(
            modifier = Modifier.wrapContentSize(),
            animatedVatInclusivePrice = 0.0f,
            rateTrendIconTint = Color.Red,
            rateTrend = RateTrend.DOWN,
            textStyle = AgilePriceCardTextStyle(
                standingChargeStyle = MaterialTheme.typography.labelLarge,
                agilePriceStyle = MaterialTheme.typography.headlineLarge,
                agilePriceUnitStyle = MaterialTheme.typography.bodyLarge,
            ),
        )
    }
}
