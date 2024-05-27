/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.unit_kwh
import kunigami.composeapp.generated.resources.unit_pound
import kunigami.composeapp.generated.resources.usage_annual_projection
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AnnualProjectionCardAdaptive(
    modifier: Modifier = Modifier,
    insights: Insights,
    layoutType: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
    when (layoutType) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium,
        -> {
            AnnualProjectionCardCompact(
                modifier = modifier,
                insights = insights,
            )
        }

        else -> {
            AnnualProjectionCardExpanded(
                modifier = modifier,
                insights = insights,
            )
        }
    }
}

@Composable
internal fun AnnualProjectionCardCompact(
    modifier: Modifier = Modifier,
    insights: Insights,
) {
    val dimension = LocalDensity.current.getDimension()
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                text = stringResource(resource = Res.string.usage_annual_projection).uppercase(),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(
                    resource = Res.string.unit_kwh,
                    insights.consumptionAnnualProjection,
                ),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(
                    resource = Res.string.unit_pound,
                    insights.costAnnualProjection.toString(precision = 2),
                ),
            )
        }
    }
}

@Composable
internal fun AnnualProjectionCardExpanded(
    modifier: Modifier = Modifier,
    insights: Insights,
) {
    val dimension = LocalDensity.current.getDimension()
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(resource = Res.string.usage_annual_projection).uppercase(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(weight = 1f),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(
                        resource = Res.string.unit_kwh,
                        insights.consumptionAnnualProjection,
                    ),
                )

                Text(
                    modifier = Modifier.weight(weight = 1f),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(
                        resource = Res.string.unit_pound,
                        insights.costAnnualProjection.toString(precision = 2),
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 24.dp),
            ) {
                AnnualProjectionCardAdaptive(
                    insights = Insights(
                        consumptionAggregateRounded = 42.290,
                        consumptionTimeSpan = 8820,
                        roughCost = 2.815,
                        consumptionAnnualProjection = 48.504,
                        costAnnualProjection = 24.868,
                        consumptionDailyAverage = 23.519,
                        costDailyAverage = 53.141,
                    ),
                    layoutType = WindowWidthSizeClass.Compact,
                )

                AnnualProjectionCardAdaptive(
                    insights = Insights(
                        consumptionAggregateRounded = 42.290,
                        consumptionTimeSpan = 8820,
                        roughCost = 2.815,
                        consumptionAnnualProjection = 48.504,
                        costAnnualProjection = 24.868,
                        consumptionDailyAverage = 23.519,
                        costDailyAverage = 53.141,
                    ),
                    layoutType = WindowWidthSizeClass.Medium,
                )

                AnnualProjectionCardAdaptive(
                    insights = Insights(
                        consumptionAggregateRounded = 42.290,
                        consumptionTimeSpan = 8820,
                        roughCost = 2.815,
                        consumptionAnnualProjection = 48.504,
                        costAnnualProjection = 24.868,
                        consumptionDailyAverage = 23.519,
                        costDailyAverage = 53.141,
                    ),
                    layoutType = WindowWidthSizeClass.Expanded,
                )
            }
        }
    }
}
