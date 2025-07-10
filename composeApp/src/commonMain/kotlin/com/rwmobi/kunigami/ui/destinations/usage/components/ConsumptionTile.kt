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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.kwh
import kunigami.composeapp.generated.resources.usage_insights_consumption
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ConsumptionTile(
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
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = AppTheme.typography.headlineMedium,
            text = insights.consumptionAggregateRounded.toString(),
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = AppTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.kwh),
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = AppTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colorScheme.onSurface,
            text = pluralStringResource(
                resource = Res.plurals.usage_insights_consumption,
                quantity = insights.consumptionTimeSpan,
                insights.consumptionTimeSpan,
            ),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        ConsumptionTile(
            modifier = Modifier
                .height(AppTheme.dimens.widgetHeight)
                .width(AppTheme.dimens.widgetWidthHalf),
            insights = InsightsSamples.trueCost,
        )
    }
}
