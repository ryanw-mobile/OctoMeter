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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.model.consumption.Insights
import com.rwmobi.kunigami.ui.previewsampledata.InsightsSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.standing_charge
import kunigami.composeapp.generated.resources.unit_pound
import kunigami.composeapp.generated.resources.usage_consumption
import kunigami.composeapp.generated.resources.usage_estimated_cost
import kunigami.composeapp.generated.resources.usage_reference_cost
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsightsTile(
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
            style = AppTheme.typography.headlineMedium,
            color = AppTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            text = stringResource(
                resource = Res.string.unit_pound,
                insights.costWithCharges.roundToTwoDecimalPlaces().toString(),
            ),
        )

        val costLabel = if (insights.isTrueCost) {
            stringResource(resource = Res.string.usage_estimated_cost)
        } else {
            stringResource(resource = Res.string.usage_reference_cost)
        }

        Text(
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colorScheme.onSurface,
            textAlign = TextAlign.Left,
            text = costLabel,
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedRatioBar(
            modifier = Modifier.fillMaxWidth(),
            consumptionRatio = insights.consumptionChargeRatio,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                style = AppTheme.typography.labelSmall,
                text = stringResource(resource = Res.string.usage_consumption),
            )
            Spacer(modifier = Modifier.width(AppTheme.dimens.grid_1))
            Text(
                style = AppTheme.typography.labelSmall,
                text = stringResource(resource = Res.string.standing_charge),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        InsightsTile(
            modifier = Modifier
                .width(AppTheme.dimens.widgetWidthFull)
                .height(AppTheme.dimens.widgetHeight),
            insights = InsightsSamples.trueCost,
        )
    }
}
