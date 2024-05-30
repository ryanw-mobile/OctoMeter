/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.ui.model.ErrorMessage

@Immutable
data class TariffsUIState(
    val isLoading: Boolean = true,
    val productSummaries: List<ProductSummary> = emptyList(),
    val productDetails: ProductDetails? = null,
    val requestedScreenType: TariffsScreenType? = null,
    val requestedLayout: TariffScreenLayout = TariffScreenLayout.Compact(useBottomSheet = true),
    val requestedWideListLayout: Boolean = false,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
