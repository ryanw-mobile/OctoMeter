/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
            InsightsCard(
                modifier = Modifier.fillMaxWidth(),
                insights = insights,
            )

            ProjectedConsumptionCard(
                modifier = Modifier.fillMaxWidth(),
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

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val maxWidthAvailable = maxWidth
        val cardCount = 1 + (if (insights != null) 2 else 0) // Count the number of cards
        val maxCardWidth = (maxWidthAvailable - (dimension.grid_1 * (cardCount - 1))) / cardCount
        val cardWidth = maxCardWidth.coerceIn(minimumValue = 0.dp, maximumValue = dimension.windowWidthCompact)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
        ) {
            tariff?.let {
                TariffSummaryCard(
                    modifier = Modifier
                        .width(width = cardWidth)
                        .fillMaxHeight(),
                    heading = stringResource(resource = Res.string.usage_applied_tariff).uppercase(),
                    tariffs = listOf(it),
                )
            }

            insights?.let { insights ->
                Spacer(modifier = Modifier.width(width = dimension.grid_1))

                InsightsCard(
                    modifier = Modifier
                        .width(width = cardWidth)
                        .fillMaxHeight(),
                    insights = insights,
                )

                Spacer(modifier = Modifier.width(width = dimension.grid_1))

                ProjectedConsumptionCard(
                    modifier = Modifier
                        .width(width = cardWidth)
                        .fillMaxHeight(),
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
