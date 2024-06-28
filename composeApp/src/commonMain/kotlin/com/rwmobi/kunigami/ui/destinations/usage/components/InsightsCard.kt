/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.WidgetCard
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.standing_charge
import kunigami.composeapp.generated.resources.unit_pound
import kunigami.composeapp.generated.resources.usage_consumption
import kunigami.composeapp.generated.resources.usage_estimated_cost
import kunigami.composeapp.generated.resources.usage_estimated_daily
import kunigami.composeapp.generated.resources.usage_insights_consumption
import kunigami.composeapp.generated.resources.usage_reference_cost
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsightsCard(
    modifier: Modifier = Modifier,
    insights: Insights,
) {
    val dimension = LocalDensity.current.getDimension()

    WidgetCard(
        modifier = modifier,
        heading = pluralStringResource(
            resource = Res.plurals.usage_insights_consumption,
            quantity = insights.consumptionTimeSpan,
            insights.consumptionAggregateRounded,
            insights.consumptionTimeSpan,
        ),
        contents = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = dimension.grid_1),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    text = stringResource(resource = Res.string.unit_pound, insights.costWithCharges.toString(precision = 2)),
                )

                val costLabel = if (insights.isTrueCost) {
                    stringResource(resource = Res.string.usage_estimated_cost)
                } else {
                    stringResource(resource = Res.string.usage_reference_cost)
                }

                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Left,
                    text = costLabel,
                )
            }

            if (insights.consumptionTimeSpan > 1) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    text = stringResource(
                        resource = Res.string.usage_estimated_daily,
                        insights.consumptionDailyAverage,
                        insights.costDailyAverage.toString(precision = 2),
                    ),
                )
            }
        },
        footer = {
            AnimatedRatioBar(
                modifier = Modifier
                    .padding(top = dimension.grid_1)
                    .defaultMinSize(minHeight = dimension.grid_2)
                    .fillMaxWidth(),
                consumptionRatio = insights.consumptionChargeRatio,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = stringResource(resource = Res.string.usage_consumption),
                )
                Spacer(modifier = Modifier.width(dimension.grid_1))
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = stringResource(resource = Res.string.standing_charge),
                )
            }
        },
    )
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        InsightsCard(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(all = 24.dp),
            insights = InsightsSamples.trueCost,
        )
    }
}
