/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import androidx.compose.runtime.Immutable

@Immutable
data class TariffsUIEvent(
    val onRefresh: () -> Unit,
    val onQueryPostcode: (postcode: String) -> Unit,
    val onProductItemClick: (productCode: String, postcode: String) -> Unit,
    val onProductDetailsDismissed: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onScrolledToTop: () -> Unit,
    val onSpecialErrorScreenShown: () -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
