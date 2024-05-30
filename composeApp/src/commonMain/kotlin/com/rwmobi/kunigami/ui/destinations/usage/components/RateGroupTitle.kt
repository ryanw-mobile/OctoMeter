/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionGroupedCells
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.unit_kwh
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RateGroupTitle(
    modifier: Modifier = Modifier,
    consumptionGroup: ConsumptionGroupedCells,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = consumptionGroup.title,
        )

        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(
                resource = Res.string.unit_kwh,
                consumptionGroup.consumptions.sumOf { it.consumption }.roundToTwoDecimalPlaces(),
            ),
        )
    }
}
