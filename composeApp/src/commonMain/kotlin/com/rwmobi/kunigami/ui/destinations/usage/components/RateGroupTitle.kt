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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionGroupWithPartitions
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.unit_kwh
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RateGroupTitle(
    modifier: Modifier = Modifier,
    consumptionGroupWithPartitions: ConsumptionGroupWithPartitions,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = consumptionGroupWithPartitions.title,
        )

        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(
                resource = Res.string.unit_kwh,
                consumptionGroupWithPartitions.partitionedItems.sumOf { partitionedItems ->
                    partitionedItems.sumOf { consumption ->
                        consumption.kWhConsumed
                    }
                }.roundToTwoDecimalPlaces(),
            ),
        )
    }
}
