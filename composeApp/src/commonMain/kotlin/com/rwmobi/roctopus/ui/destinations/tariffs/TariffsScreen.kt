/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.ui.destinations.tariffs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.roctopus.ui.components.ProductItem
import com.rwmobi.roctopus.ui.theme.getDimension

@Composable
fun TariffsScreen(
    modifier: Modifier = Modifier,
    uiState: TariffsUIState,
    uiEvent: TariffsUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val dimension = LocalDensity.current.getDimension()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        itemsIndexed(
            items = uiState.products,
            key = { _, product -> product.code },
        ) { index, product ->
            ProductItem(
                modifier = Modifier.fillMaxWidth(),
                product = product,
            )

            if (index < uiState.products.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = dimension.grid_1)
                        .fillMaxWidth(),
                )
            }
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }
}
