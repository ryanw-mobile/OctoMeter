/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionGroupedCells
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionQueryFilter
import com.rwmobi.kunigami.ui.model.consumption.Insights

@Immutable
data class UsageUIState(
    val isLoading: Boolean = true,
    val isDemoMode: Boolean? = null,
    val userProfile: UserProfile? = null,
    val showToolTipOnClick: Boolean = false,
    val requestedChartLayout: RequestedChartLayout = RequestedChartLayout.Portrait,
    val requestedAdaptiveLayout: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    val requestedUsageColumns: Int = 1,
    val consumptionQueryFilter: ConsumptionQueryFilter = ConsumptionQueryFilter(),
    val consumptionGroupedCells: List<ConsumptionGroupedCells> = emptyList(),
    val consumptionRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val barChartData: BarChartData? = null,
    val insights: Insights? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
