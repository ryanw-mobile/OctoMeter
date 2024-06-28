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
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_different_tariff
import kunigami.composeapp.generated.resources.agile_tariff_standard_unit_rate_two_lines
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge_two_lines
import kunigami.composeapp.generated.resources.standard_unit_rate
import kunigami.composeapp.generated.resources.standing_charge
import kunigami.composeapp.generated.resources.unit_p_day
import kunigami.composeapp.generated.resources.unit_p_kwh
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffSummaryCardAdaptive(
    modifier: Modifier = Modifier,
    layoutType: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    tariff: Tariff,
    heading: String,
    headingTextAlign: TextAlign = TextAlign.Start,
    subheading: String? = null,
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
                subheading = subheading,
                tariff = tariff,
            )
        }

        WindowWidthSizeClass.Medium -> {
            TariffSummaryCardLinear(
                modifier = cardModifier,
                heading = heading,
                headingTextAlign = headingTextAlign,
                subheading = subheading,
                tariff = tariff,
            )
        }

        else -> {
            TariffSummaryCardThreeColumns(
                modifier = cardModifier,
                heading = heading,
                headingTextAlign = headingTextAlign,
                subheading = subheading,
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
    subheading: String?,
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

        subheading?.let {
            Text(
                modifier = Modifier.wrapContentWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.68f,
                ),
                text = subheading,
            )
        }

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

        if (!tariff.isVariable) {
            tariff.resolveUnitRate()?.let { unitRate ->
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
                        text = stringResource(resource = Res.string.unit_p_kwh, unitRate),
                    )
                }
            }
        }
    }
}

@Composable
private fun TariffSummaryCardTwoColumns(
    modifier: Modifier = Modifier,
    heading: String,
    headingTextAlign: TextAlign,
    subheading: String?,
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

                subheading?.let {
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.68f,
                        ),
                        text = subheading,
                    )
                }
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

                if (!tariff.isVariable) {
                    Spacer(modifier = Modifier.size(size = dimension.grid_1))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        text = stringResource(resource = Res.string.agile_tariff_standard_unit_rate_two_lines, tariff.resolveUnitRate() ?: ""),
                    )
                }
            }
        }
    }
}

@Composable
private fun TariffSummaryCardThreeColumns(
    modifier: Modifier = Modifier,
    heading: String,
    headingTextAlign: TextAlign,
    subheading: String?,
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

                subheading?.let {
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.68f,
                        ),
                        text = subheading,
                    )
                }
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

            if (!tariff.isVariable) {
                tariff.resolveUnitRate()?.let { unitRate ->
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            text = stringResource(resource = Res.string.agile_tariff_standard_unit_rate_two_lines, unitRate),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        TariffSummaryCardAdaptive(
            modifier = Modifier.fillMaxWidth(),
            heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
            headingTextAlign = TextAlign.Center,
            subheading = "Sample subheading",
            tariff = TariffSamples.agileFlex221125,
            layoutType = WindowWidthSizeClass.Compact,
        )

        TariffSummaryCardAdaptive(
            modifier = Modifier.fillMaxWidth(),
            heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
            headingTextAlign = TextAlign.Center,
            subheading = "Sample subheading",
            tariff = TariffSamples.agileFlex221125,
            layoutType = WindowWidthSizeClass.Medium,
        )

        TariffSummaryCardAdaptive(
            modifier = Modifier.fillMaxWidth(),
            heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
            headingTextAlign = TextAlign.Center,
            subheading = "Sample subheading",
            tariff = TariffSamples.agileFlex221125,
            layoutType = WindowWidthSizeClass.Expanded,
        )
    }
}
