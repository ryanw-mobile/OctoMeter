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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TariffSummaryCard
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun TariffProjectionsCardAdaptive(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
    layoutType: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
//    when (layoutType) {
//        WindowWidthSizeClass.Compact,
//        WindowWidthSizeClass.Medium,
//            -> {
//            TariffProjectionsCardLinear(
//                modifier = modifier,
//                tariff = tariff,
//                insights = insights,
//            )
//        }
//
//        else -> {
    TariffProjectionsTileFlowRow(
        modifier = modifier,
        tariff = tariff,
        insights = insights,
    )
//        }
//    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TariffProjectionsTileFlowRow(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
) {
    val dimension = LocalDensity.current.getDimension()
    FlowRow(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(dimension.grid_1),
    ) {
        tariff?.let {
            TariffSummaryCard(
                modifier = Modifier
                    .width(dimension.widgetWidthFull)
                    .height(dimension.widgetHeight)
                    .padding(horizontal = dimension.grid_0_5),
                tariff = it,
            )
        }

        insights?.let { insights ->
            InsightsCard(
                modifier = Modifier
                    .width(dimension.widgetWidthFull)
                    .height(dimension.widgetHeight)
                    .padding(horizontal = dimension.grid_0_5),
                insights = insights,
            )

            ConsumptionTile(
                modifier = Modifier
                    .width(dimension.widgetWidthHalf)
                    .height(dimension.widgetHeight)
                    .padding(horizontal = dimension.grid_0_5),
                insights = insights,
            )

            if (insights.consumptionTimeSpan > 1) {
                DailyAverageTile(
                    modifier = Modifier
                        .width(dimension.widgetWidthHalf)
                        .height(dimension.widgetHeight)
                        .padding(horizontal = dimension.grid_0_5),
                    insights = insights,
                )
            }

            ProjectedConsumptionTile(
                modifier = Modifier
                    .width(dimension.widgetWidthHalf)
                    .height(dimension.widgetHeight)
                    .padding(horizontal = dimension.grid_0_5),
                insights = insights,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        TariffProjectionsCardAdaptive(
            modifier = Modifier.padding(all = 16.dp),
            tariff = TariffSamples.agileFlex221125,
            insights = InsightsSamples.trueCost,
            layoutType = WindowWidthSizeClass.Expanded,
        )

        TariffProjectionsCardAdaptive(
            modifier = Modifier.padding(all = 16.dp),
            tariff = TariffSamples.agileFlex221125,
            insights = InsightsSamples.trueCost,
            layoutType = WindowWidthSizeClass.Compact,
        )
    }
}
