/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.ui.destinations.tariffs

import com.rwmobi.roctopus.domain.model.Product
import com.rwmobi.roctopus.ui.model.ErrorMessage

data class TariffsUIState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessages: List<ErrorMessage> = emptyList(),
)
