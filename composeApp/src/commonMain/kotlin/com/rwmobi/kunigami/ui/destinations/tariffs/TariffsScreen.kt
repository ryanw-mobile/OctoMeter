/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.product.Product
import com.rwmobi.kunigami.ui.components.DualTitleBar
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ProductItem
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.composehelper.conditionalBlur
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.navigation_tariffs
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    val mainLazyListState = rememberLazyListState()

    Box(modifier = modifier) {
        if (uiState.products.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .conditionalBlur(enabled = uiState.isLoading && uiState.products.isEmpty()),
            ) {
                ScrollbarMultiplatform(
                    modifier = Modifier.weight(weight = 1f),
                    lazyListState = mainLazyListState,
                ) { contentModifier ->
                    LazyColumn(
                        modifier = contentModifier.fillMaxSize(),
                        state = mainLazyListState,
                    ) {
                        stickyHeader {
                            DualTitleBar(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .fillMaxWidth()
                                    .height(height = dimension.minListItemHeight),
                                title = stringResource(resource = Res.string.navigation_tariffs),
                            )
                        }

                        itemsIndexed(
                            items = uiState.products,
                            key = { _, product -> product.code },
                        ) { index, product ->
                            ProductItem(
                                modifier = Modifier
                                    .clickable(onClick = { uiEvent.onProductItemClick(product.code) })
                                    .fillMaxWidth()
                                    .padding(vertical = dimension.grid_1),
                                product = product,
                            )

                            if (index < uiState.products.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }

                if (uiState.requestedLayout == TariffScreenLayout.ListDetailPane) {
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    )

                    val detailLazyListState = rememberLazyListState()
                    ScrollbarMultiplatform(
                        modifier = Modifier.weight(weight = 1f),
                        lazyListState = detailLazyListState,
                    ) { contentModifier ->
                        LazyColumn(
                            modifier = contentModifier.fillMaxSize(),
                            state = detailLazyListState,
                        ) {
                            uiState.productDetails?.let { product ->
                                stickyHeader {
                                    DualTitleBar(
                                        modifier = Modifier
                                            .background(color = MaterialTheme.colorScheme.secondary)
                                            .fillMaxWidth()
                                            .height(height = dimension.minListItemHeight),
                                        title = stringResource(resource = Res.string.navigation_tariffs),
                                    )
                                }

                                item {
                                    Button(
                                        onClick = uiEvent.onProductDetailsDismissed,
                                    ) {
                                        Text("close")
                                    }
                                }

                                item {
                                    ProductItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = dimension.grid_1),
                                        product = product,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else if (!uiState.isLoading) {
            // no data
            Text("Placeholder for no data")
        }

        if (uiState.isLoading && uiState.products.isEmpty()) {
            LoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    if (bottomSheetState.isVisible && uiState.requestedLayout != TariffScreenLayout.ListDetailPane) {
        TariffBottomSheet(
            modifier = Modifier.fillMaxSize(),
            product = uiState.productDetails,
            bottomSheetState = bottomSheetState,
            onDismissRequest = uiEvent.onProductDetailsDismissed,
        )
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }

    LaunchedEffect(uiState.productDetails, uiState.requestedLayout) {
        if (uiState.requestedLayout == TariffScreenLayout.ListDetailPane) {
            if (bottomSheetState.isVisible) {
                bottomSheetState.hide()
            }
        } else if (uiState.productDetails != null) {
            if (!bottomSheetState.isVisible) {
                bottomSheetState.show()
            }
        } else {
            if (bottomSheetState.isVisible) {
                bottomSheetState.hide()
            }
        }
    }

    LaunchedEffect(uiState.requestScrollToTop) {
        if (uiState.requestScrollToTop) {
            mainLazyListState.scrollToItem(index = 0)
            uiEvent.onScrolledToTop()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TariffBottomSheet(
    modifier: Modifier = Modifier,
    product: Product?,
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
            product?.let {
                item {
                    ProductItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimension.grid_1),
                        product = it,
                    )
                }
            }
        }
    }
}
