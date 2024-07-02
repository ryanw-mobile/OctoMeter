/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.WidgetCard
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.cyanish
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.theme.purpleish
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.unit_p_kwh
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LatestTariffsCard(
    modifier: Modifier = Modifier,
    latestFixedTariff: Tariff?,
    latestFlexibleTariff: Tariff?,
    latestFixedTariffColor: Color,
    latestFlexibleTariffColor: Color,
) {
    val dimension = LocalDensity.current.getDimension()

    WidgetCard(
        modifier = modifier,
        contents = {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
                verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
            ) {
                latestFlexibleTariff?.let { tariff ->
                    tariff.vatInclusiveStandardUnitRate?.let { unitRate ->
                        TariffEntry(
                            modifier = Modifier
                                .widthIn(min = dimension.windowWidthCompactOneThird)
                                .weight(1f),
                            tariff = tariff,
                            unitRate = unitRate,
                            indicatorColor = latestFlexibleTariffColor,
                        )
                    }
                }

                latestFixedTariff?.let { tariff ->
                    tariff.vatInclusiveStandardUnitRate?.let { unitRate ->
                        TariffEntry(
                            modifier = Modifier
                                .widthIn(min = dimension.windowWidthCompactOneThird)
                                .weight(1f),
                            tariff = tariff,
                            unitRate = unitRate,
                            indicatorColor = latestFixedTariffColor,
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun TariffEntry(
    modifier: Modifier = Modifier,
    tariff: Tariff,
    unitRate: Double,
    indicatorColor: Color,
) {
    val dimension = LocalDensity.current.getDimension()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val width = dimension.grid_2.toPx()
                drawRect(
                    color = indicatorColor,
                    size = Size(width, size.height),
                )
            }
            .padding(
                vertical = dimension.grid_0_25,
                horizontal = dimension.grid_4,
            ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            text = tariff.displayName,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(
                resource = Res.string.unit_p_kwh,
                unitRate.roundToTwoDecimalPlaces().toString(),
            ),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        LatestTariffsCard(
            modifier = Modifier.wrapContentSize(),
            latestFlexibleTariff = TariffSamples.agileFlex221125,
            latestFixedTariff = TariffSamples.fix12M240411,
            latestFlexibleTariffColor = cyanish,
            latestFixedTariffColor = purpleish,
        )
    }
}
