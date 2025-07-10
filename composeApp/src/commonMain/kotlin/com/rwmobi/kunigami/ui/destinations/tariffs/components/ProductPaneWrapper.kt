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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
internal fun ProductPaneWrapper(
    modifier: Modifier,
    productDetails: ProductDetails? = null,
    header: @Composable () -> Unit,
) {
    val detailLazyListState = rememberLazyListState()
    ScrollbarMultiplatform(
        modifier = modifier,
        lazyListState = detailLazyListState,
    ) { contentModifier ->
        Column(modifier = contentModifier.fillMaxSize()) {
            header()

            val productScreenLayoutModifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimens.grid_1)

            LazyColumn(state = detailLazyListState) {
                productDetails?.let { product ->
                    productScreenLayout(
                        modifier = productScreenLayoutModifier,
                        productDetails = product,
                    )
                }
            }
        }
    }
}
