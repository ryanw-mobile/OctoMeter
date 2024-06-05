/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.extensions.generateRandomLong
import com.rwmobi.kunigami.ui.extensions.mapFromPlatform
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import io.ktor.util.network.UnresolvedAddressException
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.tariffs_error_load_tariffs
import org.jetbrains.compose.resources.getString

@Immutable
data class AccountUIState(
    val isLoading: Boolean = true,
    val requestedScreenType: AccountScreenType? = null,
    val requestedLayout: AccountScreenLayout = AccountScreenLayout.Compact,
    val userProfile: UserProfile? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    fun updateLayoutType(windowSizeClass: WindowSizeClass): AccountUIState {
        val requestedLayout = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> AccountScreenLayout.WideWrapped
            WindowWidthSizeClass.Medium -> AccountScreenLayout.Wide
            else -> AccountScreenLayout.Compact
        }

        return copy(
            requestedLayout = requestedLayout,
        )
    }

    suspend fun filterErrorAndStopLoading(throwable: Throwable): AccountUIState {
        return when (val translatedThrowable = throwable.mapFromPlatform()) {
            is HttpException -> {
                copy(
                    requestedScreenType = AccountScreenType.Error(SpecialErrorScreen.HttpError(statusCode = translatedThrowable.httpStatusCode)),
                    isLoading = false,
                )
            }

            is UnresolvedAddressException -> {
                copy(
                    requestedScreenType = AccountScreenType.Error(SpecialErrorScreen.NetworkError),
                    isLoading = false,
                )
            }

            else -> {
                handleErrorAndStopLoading(message = throwable.message ?: getString(resource = Res.string.tariffs_error_load_tariffs))
            }
        }
    }

    fun handleErrorAndStopLoading(message: String): AccountUIState {
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
