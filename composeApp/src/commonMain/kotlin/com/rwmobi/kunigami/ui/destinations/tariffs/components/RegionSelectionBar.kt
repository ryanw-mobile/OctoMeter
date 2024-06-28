/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.coroutines.launch
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.tariffs_retail_region
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RegionSelectionBar(
    modifier: Modifier = Modifier,
    selectedRegion: String,
    electricityTariffs: Map<String, Tariff>,
    onRegionSelected: (key: String) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = dimension.grid_2),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            text = stringResource(resource = Res.string.tariffs_retail_region),
        )

        // On desktop, LazyRow needs a special treatment to support dragging to scroll
        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            scrollState.scrollBy(-delta)
                        }
                    },
                ),
            contentPadding = PaddingValues(end = dimension.grid_2),
            state = scrollState,
        ) {
            electricityTariffs.keys.forEach { key ->
                item(key = key) {
                    Button(
                        onClick = { if (key != selectedRegion) onRegionSelected(key) },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = if (key == selectedRegion) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (key == selectedRegion) contentColorFor(MaterialTheme.colorScheme.primary) else MaterialTheme.colorScheme.onSurface,
                        ),
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
