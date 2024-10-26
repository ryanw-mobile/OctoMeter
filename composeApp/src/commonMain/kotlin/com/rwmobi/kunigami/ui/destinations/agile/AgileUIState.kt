/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.extensions.generateRandomLong
import com.rwmobi.kunigami.ui.extensions.getPlatformType
import com.rwmobi.kunigami.ui.extensions.mapFromPlatform
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.PlatformType
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.rate.RateGroup
import io.ktor.util.network.UnresolvedAddressException
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_tariff
import org.jetbrains.compose.resources.getString

@Immutable
data class AgileUIState(
    val isLoading: Boolean = true,
    val isDemoMode: Boolean? = null,
    val showToolTipOnClick: Boolean = false,
    val requestedScreenType: AgileScreenType = AgileScreenType.Chart,
    val requestedChartLayout: RequestedChartLayout = RequestedChartLayout.Portrait,
    val requestedRateColumns: Int = 1,
    val requestedAdaptiveLayout: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    val userProfile: UserProfile? = null,
    val agileTariff: Tariff? = null,
    val latestFixedTariff: Tariff? = null,
    val latestFlexibleTariff: Tariff? = null,
    val rateGroupedCells: List<RateGroup> = emptyList(),
    val minimumVatInclusivePrice: Double = 0.0,
    val rateRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val barChartData: BarChartData? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    private val rateColumnWidth = 175.dp

    fun updateLayoutType(screenSizeInfo: ScreenSizeInfo, windowSizeClass: WindowSizeClass): AgileUIState {
        val showToolTipOnClick = windowSizeClass.getPlatformType() != PlatformType.DESKTOP
        val usageColumns = (screenSizeInfo.widthDp / rateColumnWidth).toInt()
        val requestedLayout = if (screenSizeInfo.isPortrait()) {
            RequestedChartLayout.Portrait
        } else {
            RequestedChartLayout.LandScape(
                requestedMaxHeight = screenSizeInfo.heightDp / 2,
            )
        }

        return copy(
            showToolTipOnClick = showToolTipOnClick,
            requestedChartLayout = requestedLayout,
            requestedRateColumns = usageColumns,
            requestedAdaptiveLayout = windowSizeClass.widthSizeClass,
        )
    }

    suspend fun filterErrorAndStopLoading(throwable: Throwable): AgileUIState {
        return when (val translatedThrowable = throwable.mapFromPlatform()) {
            is HttpException -> {
                copy(
                    requestedScreenType = AgileScreenType.Error(SpecialErrorScreen.HttpError(statusCode = translatedThrowable.httpStatusCode)),
                    isLoading = false,
                )
            }

            is UnresolvedAddressException -> {
                copy(
                    requestedScreenType = AgileScreenType.Error(SpecialErrorScreen.NetworkError),
                    isLoading = false,
                )
            }

            else -> {
                handleErrorAndStopLoading(message = throwable.message ?: getString(resource = Res.string.account_error_load_tariff))
            }
        }
    }

    private fun handleErrorAndStopLoading(message: String): AgileUIState {
        val newErrorMessages = if (errorMessages.any { it.message == message }) {
            errorMessages
        } else {
            errorMessages + ErrorMessage(
                id = generateRandomLong(),
                message = message,
            )
        }
        return copy(
            errorMessages = newErrorMessages,
            isLoading = false,
        )
    }
}
