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

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.kwh
import kunigami.composeapp.generated.resources.usage_kwh_month
import kunigami.composeapp.generated.resources.usage_kwh_year
import kunigami.composeapp.generated.resources.usage_projected_consumption
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ProjectedConsumptionTile(
    modifier: Modifier = Modifier,
    insights: Insights,
) {
    Column(
        modifier = modifier
            .clip(shape = AppTheme.shapes.large)
            .background(AppTheme.colorScheme.surfaceContainer)
            .padding(AppTheme.dimens.grid_2),
        verticalArrangement = Arrangement.Top,
    ) {
        UsageBlock(
            usage = insights.consumptionAnnualProjection.roundToTwoDecimalPlaces().toString(),
            period = stringResource(resource = Res.string.usage_kwh_year),
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = AppTheme.dimens.grid_1)
                .alpha(0.5f),
        )

        UsageBlock(
            usage = (insights.consumptionAnnualProjection / 12.0).roundToTwoDecimalPlaces().toString(),
            period = stringResource(resource = Res.string.usage_kwh_month),
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(resource = Res.string.usage_projected_consumption),
            style = AppTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
private fun UsageBlock(
    usage: String,
    period: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.grid_1),
    ) {
        Text(
            modifier = Modifier.wrapContentWidth(),
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colorScheme.onSurface,
            text = usage,
        )

        Text(
            modifier = Modifier.wrapContentWidth(),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.onSurface,
            text = stringResource(resource = Res.string.kwh),
        )
    }

    Text(
        modifier = Modifier.fillMaxWidth(),
        style = AppTheme.typography.labelMedium,
        color = AppTheme.colorScheme.onSurface.copy(
            alpha = 0.5f,
        ),
        text = period,
    )
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        ProjectedConsumptionTile(
            modifier = Modifier.height(AppTheme.dimens.widgetHeight)
                .width(AppTheme.dimens.widgetWidthHalf),
            insights = InsightsSamples.trueCost,
        )
    }
}
