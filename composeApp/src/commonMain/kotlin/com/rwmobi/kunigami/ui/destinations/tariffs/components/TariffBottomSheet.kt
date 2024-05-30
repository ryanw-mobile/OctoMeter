/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.ui.theme.getDimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TariffBottomSheet(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails?,
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        LazyColumn {
            productDetails?.let {
                productDetailsLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimension.grid_1),
                    productDetails = it,
                )
            }
        }
    }
}
