/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_different_tariff
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.agile_tariff_standard_unit_rate
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MyCurrentTariffCard(
    modifier: Modifier = Modifier,
    tariff: Tariff,
    useWideLayout: Boolean = false,
) {
    if (useWideLayout) {
        MyCurrentTariffCardWide(
            modifier = modifier,
            tariff = tariff,
        )
    } else {
        MyCurrentTariffCardCompact(
            modifier = modifier,
            tariff = tariff,
        )
    }
}

@Composable
private fun MyCurrentTariffCardCompact(
    modifier: Modifier = Modifier,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(all = dimension.grid_2),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            text = tariff.displayName,
        )

        val regionCode = tariff.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(resource = Res.string.agile_product_code_retail_region, tariff.productCode, regionCode),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(resource = Res.string.agile_tariff_standing_charge, tariff.vatInclusiveStandingCharge),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_0_5))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(resource = Res.string.agile_tariff_standard_unit_rate, tariff.vatInclusiveUnitRate),
        )
    }
}

@Composable
private fun MyCurrentTariffCardWide(
    modifier: Modifier = Modifier,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(all = dimension.grid_2),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
        ) {
            Column(
                modifier = Modifier.wrapContentWidth(),
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    text = tariff.displayName,
                )

                val regionCode = tariff.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(resource = Res.string.agile_product_code_retail_region, tariff.productCode, regionCode),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(resource = Res.string.agile_tariff_standing_charge, tariff.vatInclusiveStandingCharge),
                )

                Spacer(modifier = Modifier.size(size = dimension.grid_0_5))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(resource = Res.string.agile_tariff_standard_unit_rate, tariff.vatInclusiveUnitRate),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 8.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                MyCurrentTariffCard(
                    modifier = Modifier.fillMaxWidth(),
                    tariff = TariffSamples.agileFlex221125,
                    useWideLayout = false,
                )

                MyCurrentTariffCard(
                    modifier = Modifier.fillMaxWidth(),
                    tariff = TariffSamples.agileFlex221125,
                    useWideLayout = true,
                )
            }
        }
    }
}
