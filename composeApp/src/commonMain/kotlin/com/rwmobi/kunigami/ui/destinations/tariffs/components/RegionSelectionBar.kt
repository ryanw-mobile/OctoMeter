/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.model.product.TariffDetails
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.tariffs_retail_region
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RegionSelectionBar(
    modifier: Modifier = Modifier,
    selectedRegion: String,
    electricityTariffs: Map<String, TariffDetails>,
    onRegionSelected: (key: String) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            text = stringResource(resource = Res.string.tariffs_retail_region),
        )

        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            electricityTariffs.keys.forEach { key ->
                if (key != selectedRegion) {
                    TextButton(
                        onClick = { onRegionSelected(key) },
                    ) {
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = key.replace(oldValue = "_", newValue = ""),
                        )
                    }
                } else {
                    Button(
                        onClick = { },
                    ) {
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = key.replace(oldValue = "_", newValue = ""),
                        )
                    }
                }
            }
        }
    }
}
