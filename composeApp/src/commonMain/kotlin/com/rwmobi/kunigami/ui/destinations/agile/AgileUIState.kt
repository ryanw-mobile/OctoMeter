/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells

@Immutable
data class AgileUIState(
    val isLoading: Boolean = true,
    val isDemoMode: Boolean? = null,
    val requestedChartLayout: RequestedChartLayout = RequestedChartLayout.Portrait,
    val requestedRateColumns: Int = 1,
    val requestedAdaptiveLayout: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    val userProfile: UserProfile? = null,
    val agileTariff: Tariff? = null,
    val rateGroupedCells: List<RateGroupedCells> = emptyList(),
    val rateRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val barChartData: BarChartData? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    fun isCurrentlyOnDifferentTariff(): Boolean {
        return (
            false == isDemoMode &&
                userProfile?.tariff != null &&
                userProfile.tariff.tariffCode != agileTariff?.tariffCode
            )
    }
}
