/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.Rate
import com.rwmobi.kunigami.ui.model.ErrorMessage

@Immutable
data class AgileUIState(
    val isLoading: Boolean = true,
    val requestedLayout: AgileScreenLayout = AgileScreenLayout.Portrait,
    val rates: List<Rate> = emptyList(),
    val rateRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
