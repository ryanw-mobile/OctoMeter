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

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductBottomSheetWrapper(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails?,
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        containerColor = CardDefaults.cardColors().containerColor,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        val productScreenLayoutModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.grid_1)

        LazyColumn {
            productDetails?.let {
                productScreenLayout(
                    modifier = productScreenLayoutModifier,
                    productDetails = it,
                )
            }
        }
    }
}
