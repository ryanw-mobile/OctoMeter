/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionGrouping
import com.rwmobi.kunigami.ui.model.BarChartData
import com.rwmobi.kunigami.ui.model.ConsumptionGroup
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.RequestedChartLayout
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Immutable
data class UsageUIState(
    val isLoading: Boolean = true,
    val requestedChartLayout: RequestedChartLayout = RequestedChartLayout.Portrait,
    val requestedUsageColumns: Int = 1,
    val grouping: ConsumptionGrouping = ConsumptionGrouping.HALF_HOURLY,
    val pointOfReference: Instant = Clock.System.now(),
    val requestedStart: Instant = Clock.System.now(),
    val requestedEnd: Instant = Clock.System.now(),
    val canNavigateBack: Boolean = false,
    val canNavigateForward: Boolean = false,
    val consumptions: List<ConsumptionGroup> = emptyList(),
    val consumptionRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val barChartData: BarChartData? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    fun getConsumptionPeriodString(): String {
        return when (grouping) {
            ConsumptionGrouping.HALF_HOURLY -> {
                requestedStart.toLocalDateString()
            }

            ConsumptionGrouping.DAY -> {
                "${requestedStart.toLocalDateString()} - ${requestedEnd.toLocalDateString()}"
            }

            ConsumptionGrouping.WEEK -> {
                "${requestedStart.toLocalDateString()} - ${requestedEnd.toLocalDateString()}"
            }

            ConsumptionGrouping.MONTH -> {
                ""
            }

            ConsumptionGrouping.QUARTER -> {
                ""
            }
        }
    }
}
