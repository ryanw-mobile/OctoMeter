/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.ui.extensions.generateRandomLong
import com.rwmobi.kunigami.ui.extensions.getPlatformType
import com.rwmobi.kunigami.ui.extensions.mapFromPlatform
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.PlatformType
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import io.ktor.util.network.UnresolvedAddressException
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
import org.jetbrains.compose.resources.getString

@Immutable
data class TariffsUIState(
    val isLoading: Boolean = true,
    val productSummaries: List<ProductSummary> = emptyList(),
    val productDetails: ProductDetails? = null,
    val requestedScreenType: TariffsScreenType? = null,
    val requestedLayout: TariffScreenLayout = TariffScreenLayout.Compact(useBottomSheet = true),
    val requestedWideListLayout: Boolean = false,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    private val windowWidthCompact: Dp = 599.dp

    fun shouldUseBottomSheet(): Boolean {
        return with(requestedLayout) {
            (this is TariffScreenLayout.Wide && useBottomSheet) ||
                (this is TariffScreenLayout.Compact && useBottomSheet)
        }
    }

    // Make it less intrusive when hopping among products
    fun shouldShowLoadingScreen() = isLoading && productSummaries.isEmpty()

    fun updateLayoutType(screenSizeInfo: ScreenSizeInfo, windowSizeClass: WindowSizeClass): TariffsUIState {
        val platform = windowSizeClass.getPlatformType()
        val requestedLayout = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> TariffScreenLayout.Compact(useBottomSheet = platform != PlatformType.DESKTOP)
            WindowWidthSizeClass.Medium -> TariffScreenLayout.Wide(useBottomSheet = platform != PlatformType.DESKTOP)
            else -> TariffScreenLayout.ListDetailPane
        }
        val requestedWideListLayout = when {
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> false
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> true
            (screenSizeInfo.widthDp / 2) > windowWidthCompact -> true // List pane width
            else -> false
        }

        return copy(
            requestedLayout = requestedLayout,
            requestedWideListLayout = requestedWideListLayout,
        ) // .updateScreenType()
    }

    fun updateScreenType(): TariffsUIState {
        return copy(
            requestedScreenType = when {
                // Error Screen is kept until being told to dismiss
                isErrorScreen() -> requestedScreenType
                shouldShowTariffsList() -> TariffsScreenType.List
                hasProductDetailsLoaded() -> TariffsScreenType.FullScreenDetail
                else -> requestedScreenType // nothing triggered for a change, just keep it
            },
        )
    }

    suspend fun filterErrorAndStopLoading(throwable: Throwable): TariffsUIState {
        return when (val translatedThrowable = throwable.mapFromPlatform()) {
            is HttpException -> {
                copy(
                    requestedScreenType = TariffsScreenType.Error(SpecialErrorScreen.HttpError(statusCode = translatedThrowable.httpStatusCode)),
                    isLoading = false,
                )
            }

            is UnresolvedAddressException -> {
                copy(
                    requestedScreenType = TariffsScreenType.Error(SpecialErrorScreen.NetworkError),
                    isLoading = false,
                )
            }

            else -> {
                handleErrorAndStopLoading(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
            }
        }
    }

    private fun handleErrorAndStopLoading(message: String): TariffsUIState {
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

    private fun shouldShowTariffsList(): Boolean {
        return productDetails == null ||
            requestedLayout is TariffScreenLayout.ListDetailPane ||
            requestedLayout == TariffScreenLayout.Compact(useBottomSheet = true) ||
            requestedLayout == TariffScreenLayout.Wide(useBottomSheet = true)
    }

    private fun isErrorScreen(): Boolean {
        return requestedScreenType is TariffsScreenType.Error
    }

    private fun hasProductDetailsLoaded(): Boolean {
        return productDetails != null
    }
}
