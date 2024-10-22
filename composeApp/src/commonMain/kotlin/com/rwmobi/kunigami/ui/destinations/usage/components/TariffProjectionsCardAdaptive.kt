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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TariffSummaryCard
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.usage_applied_tariff
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffProjectionsCardAdaptive(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
    layoutType: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
    when (layoutType) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium,
        -> {
            TariffProjectionsCardLinear(
                modifier = modifier,
                tariff = tariff,
                insights = insights,
            )
        }

        else -> {
            TariffProjectionsCardThreeColumns(
                modifier = modifier,
                tariff = tariff,
                insights = insights,
            )
        }
    }
}

@Composable
private fun TariffProjectionsCardLinear(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        tariff?.let {
            TariffSummaryCard(
                modifier = Modifier.fillMaxWidth(),
                heading = stringResource(resource = Res.string.usage_applied_tariff).uppercase(),
                tariffs = listOf(it),
            )
        }

        insights?.let { insights ->
            HorizontalDivider(
                modifier = Modifier.height(height = dimension.grid_1),
                thickness = Dp.Hairline,
            )

            InsightsCard(
                modifier = Modifier.fillMaxWidth(),
                insights = insights,
            )

            ProjectedConsumptionTile(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimension.widgetHeight),
                insights = insights,
            )
        }
    }
}

@Composable
private fun TariffProjectionsCardThreeColumns(
    modifier: Modifier = Modifier,
    tariff: Tariff?,
    insights: Insights?,
) {
    val dimension = LocalDensity.current.getDimension()
    val cardCount = 1 + (if (insights != null) 2 else 0)
    val maxRowWidth = dimension.windowWidthCompact * cardCount

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .widthIn(max = maxRowWidth)
                .align(alignment = Alignment.Center),
            horizontalArrangement = Arrangement.Center,
        ) {
            tariff?.let {
                TariffSummaryCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    heading = stringResource(resource = Res.string.usage_applied_tariff).uppercase(),
                    tariffs = listOf(it),
                )
            }

            insights?.let { insights ->
                VerticalDivider(
                    modifier = Modifier.width(width = dimension.grid_1),
                    thickness = Dp.Hairline,
                )

                InsightsCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    insights = insights,
                )

                ProjectedConsumptionTile(
                    modifier = Modifier
                        .width(dimension.widgetWidthFull)
                        .height(dimension.widgetHeight),
                    insights = insights,
                )
            }
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
