/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kunigami.composeapp.generated.resources.unit_pound
import kunigami.composeapp.generated.resources.usage_estimated_cost
import kunigami.composeapp.generated.resources.usage_estimated_daily
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
                .fillMaxSize()
                .padding(all = dimension.grid_2),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                text = pluralStringResource(
                    resource = Res.plurals.usage_insights_consumption,
                    quantity = insights.consumptionTimeSpan,
                    insights.consumptionAggregateRounded,
                    insights.consumptionTimeSpan,
                ),
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.unit_pound, insights.roughCost.toString(precision = 2)),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.usage_estimated_cost),
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

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
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            InsightsCard(
                modifier = Modifier
                    .height(intrinsicSize = IntrinsicSize.Min)
                    .padding(all = 24.dp),
                insights = Insights(
                    consumptionAggregateRounded = 86.693,
                    consumptionTimeSpan = 2084,
                    roughCost = 2880.027,
                    consumptionDailyAverage = 71.227,
                    costDailyAverage = 52.218,
                    consumptionAnnualProjection = 82.473,
                    costAnnualProjection = 4.136,
                ),
            )
        }
    }
}
