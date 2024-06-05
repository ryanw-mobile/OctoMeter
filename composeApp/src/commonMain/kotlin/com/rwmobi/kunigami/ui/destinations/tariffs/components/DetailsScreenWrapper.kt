/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun DetailsScreenWrapper(
    modifier: Modifier,
    productDetails: ProductDetails? = null,
    header: @Composable () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    val detailLazyListState = rememberLazyListState()
    ScrollbarMultiplatform(
        modifier = modifier,
        lazyListState = detailLazyListState,
    ) { contentModifier ->
        Column(
            modifier = contentModifier.fillMaxSize(),
        ) {
            header()

            LazyColumn(
                state = detailLazyListState,
            ) {
                productDetails?.let { product ->
                    productDetailsLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimension.grid_1),
                        productDetails = product,
                    )
                }
            }
        }
    }
}
