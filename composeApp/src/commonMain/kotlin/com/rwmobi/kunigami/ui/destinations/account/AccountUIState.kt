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
    val requestedLayout: AccountScreenLayoutStyle = AccountScreenLayoutStyle.Compact,
    val userProfile: UserProfile? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    fun updateLayoutType(windowSizeClass: WindowSizeClass): AccountUIState {
        val requestedLayout = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> AccountScreenLayoutStyle.WideWrapped
            WindowWidthSizeClass.Medium -> AccountScreenLayoutStyle.Wide
            else -> AccountScreenLayoutStyle.Compact
        }

        return copy(
            requestedLayout = requestedLayout,
        )
    }

    suspend fun filterErrorAndStopLoading(throwable: Throwable): AccountUIState = when (val translatedThrowable = throwable.mapFromPlatform()) {
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
            handleMessageAndStopLoading(message = throwable.message ?: getString(resource = Res.string.tariffs_error_load_tariffs))
        }
    }

    fun handleMessageAndStopLoading(message: String): AccountUIState {
        val newMessages = if (errorMessages.any { it.message == message }) {
            errorMessages
        } else {
            errorMessages + ErrorMessage(
                id = generateRandomLong(),
                message = message,
            )
        }
        return copy(
            errorMessages = newMessages,
            isLoading = false,
        )
    }
}
