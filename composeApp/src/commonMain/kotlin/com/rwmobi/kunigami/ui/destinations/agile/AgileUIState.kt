/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.ui.model.BarChartData
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells

@Immutable
data class AgileUIState(
    val isLoading: Boolean = true,
    val requestedChartLayout: RequestedChartLayout = RequestedChartLayout.Portrait,
    val requestedRateColumns: Int = 1,
    val rateGroupedCells: List<RateGroupedCells> = emptyList(),
    val rateRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val barChartData: BarChartData? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
