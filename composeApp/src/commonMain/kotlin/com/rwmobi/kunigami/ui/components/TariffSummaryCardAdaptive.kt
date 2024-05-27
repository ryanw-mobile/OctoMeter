/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_different_tariff
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.agile_tariff_standard_unit_rate_two_lines
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge_two_lines
import kunigami.composeapp.generated.resources.standard_unit_rate
import kunigami.composeapp.generated.resources.standing_charge
import kunigami.composeapp.generated.resources.unit_p_day
import kunigami.composeapp.generated.resources.unit_p_kwh
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffSummaryCardAdaptive(
    modifier: Modifier = Modifier,
    layoutType: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    heading: String,
    headingTextAlign: TextAlign = TextAlign.Start,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()
    val cardModifier = modifier
        .clip(shape = MaterialTheme.shapes.medium)
        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
        .padding(
            vertical = dimension.grid_2,
            horizontal = dimension.grid_2,
        )

    when (layoutType) {
        WindowWidthSizeClass.Compact -> {
            TariffSummaryCardLinear(
                modifier = cardModifier,
                heading = heading,
                headingTextAlign = headingTextAlign,
                tariff = tariff,
            )
        }

        WindowWidthSizeClass.Medium -> {
            TariffSummaryCardLinear(
                modifier = cardModifier,
                heading = heading,
                headingTextAlign = headingTextAlign,
                tariff = tariff,
            )
        }

        else -> {
            TariffSummaryCardThreeColumns(
                modifier = cardModifier,
                heading = heading,
                headingTextAlign = headingTextAlign,
                tariff = tariff,
            )
        }
    }
}

@Composable
private fun TariffSummaryCardLinear(
    modifier: Modifier = Modifier,
    heading: String,
    headingTextAlign: TextAlign,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            textAlign = headingTextAlign,
            color = MaterialTheme.colorScheme.onSurface,
            text = heading,
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
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.68f,
            ),
            text = stringResource(resource = Res.string.agile_product_code_retail_region, tariff.productCode, regionCode),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_2))

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

        Spacer(modifier = Modifier.size(size = dimension.grid_0_5))

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
                text = stringResource(resource = Res.string.unit_p_kwh, tariff.vatInclusiveUnitRate),
            )
        }
    }
}

@Composable
private fun TariffSummaryCardTwoColumns(
    modifier: Modifier = Modifier,
    heading: String,
    headingTextAlign: TextAlign,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            textAlign = headingTextAlign,
            color = MaterialTheme.colorScheme.onSurface,
            text = heading,
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Column(
                modifier = Modifier.weight(1f),
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
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.68f,
                    ),
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
                    textAlign = TextAlign.End,
                    text = stringResource(resource = Res.string.agile_tariff_standing_charge_two_lines, tariff.vatInclusiveStandingCharge),
                )

                Spacer(modifier = Modifier.size(size = dimension.grid_1))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                    text = stringResource(resource = Res.string.agile_tariff_standard_unit_rate_two_lines, tariff.vatInclusiveUnitRate),
                )
            }
        }
    }
}

@Composable
private fun TariffSummaryCardThreeColumns(
    modifier: Modifier = Modifier,
    heading: String,
    headingTextAlign: TextAlign,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            textAlign = headingTextAlign,
            color = MaterialTheme.colorScheme.onSurface,
            text = heading,
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Column(
                modifier = Modifier.weight(1f),
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
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.68f,
                    ),
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
                    textAlign = TextAlign.Center,
                    text = stringResource(resource = Res.string.agile_tariff_standing_charge_two_lines, tariff.vatInclusiveStandingCharge),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    text = stringResource(resource = Res.string.agile_tariff_standard_unit_rate_two_lines, tariff.vatInclusiveUnitRate),
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
                TariffSummaryCardAdaptive(
                    modifier = Modifier.fillMaxWidth(),
                    heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                    headingTextAlign = TextAlign.Center,
                    tariff = TariffSamples.agileFlex221125,
                    layoutType = WindowWidthSizeClass.Compact,
                )

                TariffSummaryCardAdaptive(
                    modifier = Modifier.fillMaxWidth(),
                    heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                    headingTextAlign = TextAlign.Center,
                    tariff = TariffSamples.agileFlex221125,
                    layoutType = WindowWidthSizeClass.Medium,
                )

                TariffSummaryCardAdaptive(
                    modifier = Modifier.fillMaxWidth(),
                    heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                    headingTextAlign = TextAlign.Center,
                    tariff = TariffSamples.agileFlex221125,
                    layoutType = WindowWidthSizeClass.Expanded,
                )
            }
        }
    }
}
