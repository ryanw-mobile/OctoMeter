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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.kwh
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
            AnnualProjectionCardLinear(
                modifier = modifier,
                insights = insights,
            )
        }

        else -> {
            AnnualProjectionCardTwoColumns(
                modifier = modifier,
                insights = insights,
            )
        }
    }
}

@Composable
private fun AnnualProjectionCardLinear(
    modifier: Modifier = Modifier,
    insights: Insights,
) {
    val dimension = LocalDensity.current.getDimension()
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.usage_annual_projection).uppercase(),
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = insights.consumptionAnnualProjection.toString(),
                )
                Spacer(modifier = Modifier.width(width = dimension.grid_0_5))
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(resource = Res.string.kwh),
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                text = stringResource(
                    resource = Res.string.unit_pound,
                    insights.costAnnualProjection.toString(precision = 2),
                ),
            )

            Spacer(modifier = Modifier.weight(weight = 1f))
        }
    }
}

@Composable
private fun AnnualProjectionCardTwoColumns(
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
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.usage_annual_projection).uppercase(),
            )

            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(weight = 1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = insights.consumptionAnnualProjection.toString(),
                    )
                    Spacer(modifier = Modifier.width(width = dimension.grid_0_5))
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = stringResource(resource = Res.string.kwh),
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                )
                Text(
                    modifier = Modifier.weight(weight = 1f),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
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
