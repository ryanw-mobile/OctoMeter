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

package com.rwmobi.kunigami.ui.components

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.standard_unit_rate
import kunigami.composeapp.generated.resources.standing_charge
import kunigami.composeapp.generated.resources.tariffs_variable
import kunigami.composeapp.generated.resources.unit_p_day
import kunigami.composeapp.generated.resources.unit_p_kwh
import kunigami.composeapp.generated.resources.usage_applied_tariff
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffSummaryTile(
    modifier: Modifier = Modifier,
    tariff: Tariff,
) {
    val dimension = getScreenSizeInfo().getDimension()
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(dimension.grid_2),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimension.grid_1),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            text = tariff.displayName,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
        ) {
            Text(
                modifier = Modifier.weight(weight = 1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(resource = Res.string.standing_charge),
            )
            Text(
                modifier = Modifier.wrapContentWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(resource = Res.string.unit_p_day, tariff.vatInclusiveStandingCharge),
            )
        }

        val resolvedUnitRate = tariff.resolveUnitRate()
        val rateString = when {
            tariff.isVariable -> stringResource(resource = Res.string.tariffs_variable)
            resolvedUnitRate != null -> stringResource(resource = Res.string.unit_p_kwh, resolvedUnitRate)
            else -> null
        }

        if (rateString != null) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = dimension.grid_1)
                    .alpha(0.5f),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
            ) {
                Text(
                    modifier = Modifier.weight(weight = 1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(resource = Res.string.standard_unit_rate),
                )
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = rateString,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal,
            text = stringResource(resource = Res.string.usage_applied_tariff),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimension.grid_1),
        ) {
            TariffSummaryTile(
                modifier = Modifier
                    .width(dimension.widgetWidthFull)
                    .height(dimension.widgetHeight),
                tariff = TariffSamples.var221101,
            )

            TariffSummaryTile(
                modifier = Modifier
                    .width(dimension.widgetWidthFull)
                    .height(dimension.widgetHeight),
                tariff = TariffSamples.fix12M240411,
            )
        }
    }
}
