/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.ui.destinations.tariffs

import com.rwmobi.kunigame.domain.model.Product
import com.rwmobi.kunigame.ui.model.ErrorMessage

data class TariffsUIState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessages: List<ErrorMessage> = emptyList(),
)
