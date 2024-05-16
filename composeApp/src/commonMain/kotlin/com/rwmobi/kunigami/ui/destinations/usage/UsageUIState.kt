/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.ui.model.ErrorMessage

data class UsageUIState(
    val isLoading: Boolean = true,
    val requestedLayout: UsageScreenLayout = UsageScreenLayout.Portrait,
    val consumptions: List<Consumption> = emptyList(),
    val consumptionRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
