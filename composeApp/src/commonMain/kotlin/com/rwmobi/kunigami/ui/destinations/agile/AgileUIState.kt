/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.TariffSummary
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
    val activeTariffSummary: TariffSummary? = null,
    val agileTariffSummary: TariffSummary? = null,
    val rateGroupedCells: List<RateGroup> = emptyList(),
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

    fun isOnDifferentTariff() = (
        false == isDemoMode &&
            activeTariffSummary != null &&
            activeTariffSummary.tariffCode != agileTariffSummary?.tariffCode
        )

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
