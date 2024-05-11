/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.ui.model.ErrorMessage

data class TariffsUIState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessages: List<ErrorMessage> = emptyList(),
)
