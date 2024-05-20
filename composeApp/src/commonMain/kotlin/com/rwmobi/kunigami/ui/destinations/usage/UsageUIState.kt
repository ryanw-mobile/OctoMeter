/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.ui.model.BarChartData
import com.rwmobi.kunigami.ui.model.ConsumptionGroup
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.RequestedChartLayout

@Immutable
data class UsageUIState(
    val isLoading: Boolean = true,
    val requestedChartLayout: RequestedChartLayout = RequestedChartLayout.Portrait,
    val requestedUsageColumns: Int = 1,
    val consumptions: List<ConsumptionGroup> = emptyList(),
    val consumptionRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val barChartData: BarChartData? = null,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
