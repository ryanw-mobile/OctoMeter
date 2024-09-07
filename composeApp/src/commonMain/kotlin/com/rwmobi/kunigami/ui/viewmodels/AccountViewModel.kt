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

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.account.ClearCacheUseCase
import com.rwmobi.kunigami.domain.usecase.account.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.account.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.account.UpdateMeterPreferenceUseCase
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenType
import com.rwmobi.kunigami.ui.destinations.account.AccountUIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_cache_success
import kunigami.composeapp.generated.resources.account_error_update_credentials
import org.jetbrains.compose.resources.StringResource

class AccountViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val initialiseAccountUseCase: InitialiseAccountUseCase,
    private val updateMeterPreferenceUseCase: UpdateMeterPreferenceUseCase,
    private val syncUserProfileUseCase: SyncUserProfileUseCase,
    private val clearCacheUseCase: ClearCacheUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AccountUIState> = MutableStateFlow(AccountUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun notifyWindowSizeClassChanged(windowSizeClass: WindowSizeClass) {
        _uiState.update { currentUiState ->
            currentUiState.updateLayoutType(
                windowSizeClass = windowSizeClass,
            )
        }
    }

    fun refresh() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            syncUserProfileUseCase().fold(
                onSuccess = { userProfile ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            userProfile = userProfile,
                            requestedScreenType = AccountScreenType.Account,
                            isLoading = false,
                        )
                    }
                },
                onFailure = { throwable ->
                    if (throwable is IncompleteCredentialsException) {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                userProfile = null,
                                requestedScreenType = AccountScreenType.Onboarding,
                                isLoading = false,
                            )
                        }
                    } else {
                        Logger.e("Unable to retrieve account details", throwable = throwable, tag = "AccountViewModel")
                        _uiState.update { currentUiState ->
                            currentUiState.filterErrorAndStopLoading(throwable = throwable)
                        }
                    }
                },
            )
        }
    }

    fun clearCredentials() {
        viewModelScope.launch {
            userPreferencesRepository.clearCredentials()
            refresh()
        }
    }

    fun submitCredentials(
        apiKey: String,
        accountNumber: String,
        stringResolver: suspend (resId: StringResource) -> String,
    ) {
        startLoading()
        viewModelScope.launch {
            val result = initialiseAccountUseCase(apiKey = apiKey, accountNumber = accountNumber)
            result.fold(
                onSuccess = { refresh() },
                onFailure = { throwable ->
                    // There is no retry for this case.
                    Logger.e("Access denied. Credentials not updated", throwable = throwable, tag = "AccountViewModel")
                    _uiState.update { currentUiState ->
                        currentUiState.handleMessageAndStopLoading(message = stringResolver(Res.string.account_error_update_credentials))
                    }
                },
            )
        }
    }

    fun updateMeterSerialNumber(
        mpan: String,
        meterSerialNumber: String,
    ) {
        startLoading()
        viewModelScope.launch {
            val result = updateMeterPreferenceUseCase(mpan = mpan, meterSerialNumber = meterSerialNumber)
            result.fold(
                onSuccess = { refresh() },
                onFailure = { throwable ->
                    Logger.e("Unable to retrieve account details", throwable = throwable, tag = "AccountViewModel")
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable)
                    }
                },
            )
        }
    }

    fun onSpecialErrorScreenShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestedScreenType = null,
            )
        }
    }

    fun requestScrollToTop(enabled: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestScrollToTop = enabled,
            )
        }
    }

    fun onClearCache(
        stringResolver: suspend (resId: StringResource) -> String,
    ) {
        startLoading()
        viewModelScope.launch {
            clearCacheUseCase().fold(
                onSuccess = {
                    _uiState.update { currentUiState ->
                        currentUiState.handleMessageAndStopLoading(message = stringResolver(Res.string.account_clear_cache_success))
                    }
                },
                onFailure = { throwable ->
                    Logger.e("Unable to clear the cache", throwable = throwable, tag = "AccountViewModel")
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable)
                    }
                },
            )
        }
    }

    private fun startLoading() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("AccountViewModel", message = { "onCleared" })
    }
}
