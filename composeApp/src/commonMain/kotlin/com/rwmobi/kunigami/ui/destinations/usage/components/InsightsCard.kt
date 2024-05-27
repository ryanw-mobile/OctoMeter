/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.usage_estimated_cost
import kunigami.composeapp.generated.resources.usage_estimated_daily
import kunigami.composeapp.generated.resources.usage_insights
import kunigami.composeapp.generated.resources.usage_insights_consumption
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsightsCard(
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
                text = stringResource(resource = Res.string.usage_insights).uppercase(),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = pluralStringResource(
                    resource = Res.plurals.usage_insights_consumption,
                    quantity = insights.consumptionTimeSpan,
                    insights.consumptionAggregateRounded,
                    insights.consumptionTimeSpan,
                ),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(
                    resource = Res.string.usage_estimated_cost,
                    insights.roughCost.toString(precision = 2),
                ),
            )

            if (insights.consumptionTimeSpan > 1) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(
                        resource = Res.string.usage_estimated_daily,
                        insights.consumptionDailyAverage,
                        insights.costDailyAverage.toString(precision = 2),
                    ),
                )
            }
        }
    }
}
