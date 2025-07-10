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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TariffSummaryTile
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
internal fun TariffProjectionTilesAdaptive(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
    layoutType: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
    when (layoutType) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium,
        -> {
            TariffProjectionsTileFLinear(
                modifier = modifier,
                tariff = tariff,
                insights = insights,
            )
        }

        else -> {
            TariffProjectionsTileFlowRow(
                modifier = modifier,
                tariff = tariff,
                insights = insights,
            )
        }
    }
}

@Composable
private fun TariffProjectionsTileFLinear(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.grid_1),
    ) {
        val fullWidthModifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.widgetHeight)

        tariff?.let {
            TariffSummaryTile(
                modifier = fullWidthModifier,
                tariff = it,
            )
        }

        insights?.let { insights ->
            InsightsTile(
                modifier = fullWidthModifier,
                insights = insights,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.grid_1),
            ) {
                val halfWidthModifier = Modifier
                    .weight(1f)
                    .height(AppTheme.dimens.widgetHeight)

                if (insights.consumptionTimeSpan > 1) {
                    ConsumptionTile(
                        modifier = halfWidthModifier,
                        insights = insights,
                    )

                    DailyAverageTile(
                        modifier = halfWidthModifier,
                        insights = insights,
                    )
                } else {
                    ConsumptionTile(
                        modifier = halfWidthModifier,
                        insights = insights,
                    )

                    ProjectedConsumptionTile(
                        modifier = halfWidthModifier,
                        insights = insights,
                    )
                }
            }

            if (insights.consumptionTimeSpan > 1) {
                ProjectedConsumptionTile(
                    modifier = fullWidthModifier,
                    insights = insights,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TariffProjectionsTileFlowRow(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
) {
    val halfWidthModifier = Modifier
        .width(AppTheme.dimens.widgetWidthHalf)
        .height(AppTheme.dimens.widgetHeight)
        .padding(horizontal = AppTheme.dimens.grid_0_5)

    val fullWidthModifier = Modifier
        .width(AppTheme.dimens.widgetWidthFull)
        .height(AppTheme.dimens.widgetHeight)
        .padding(horizontal = AppTheme.dimens.grid_0_5)

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.grid_1),
    ) {
        tariff?.let {
            TariffSummaryTile(
                modifier = fullWidthModifier,
                tariff = it,
            )
        }

        insights?.let { insights ->
            InsightsTile(
                modifier = fullWidthModifier,
                insights = insights,
            )

            ConsumptionTile(
                modifier = halfWidthModifier,
                insights = insights,
            )

            if (insights.consumptionTimeSpan > 1) {
                DailyAverageTile(
                    modifier = halfWidthModifier,
                    insights = insights,
                )
            }

            ProjectedConsumptionTile(
                modifier = halfWidthModifier,
                insights = insights,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        TariffProjectionTilesAdaptive(
            modifier = Modifier.padding(all = 16.dp),
            tariff = TariffSamples.agileFlex221125,
            insights = InsightsSamples.trueCost,
            layoutType = WindowWidthSizeClass.Expanded,
        )

        TariffProjectionTilesAdaptive(
            modifier = Modifier.padding(all = 16.dp),
            tariff = TariffSamples.agileFlex221125,
            insights = InsightsSamples.trueCost,
            layoutType = WindowWidthSizeClass.Compact,
        )
    }
}
