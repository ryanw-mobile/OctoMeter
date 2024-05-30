/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.UpdateMeterPreferenceUseCase
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenType
import com.rwmobi.kunigami.ui.destinations.account.AccountUIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
import kunigami.composeapp.generated.resources.account_error_update_credentials
import org.jetbrains.compose.resources.getString

class AccountViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val initialiseAccountUseCase: InitialiseAccountUseCase,
    private val updateMeterPreferenceUseCase: UpdateMeterPreferenceUseCase,
    private val syncUserProfileUseCase: SyncUserProfileUseCase,
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
                                requestedScreenType = AccountScreenType.Onboarding,
                                isLoading = false,
                            )
                        }
                    } else {
                        Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
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
    ) {
        startLoading()
        viewModelScope.launch {
            val result = initialiseAccountUseCase(apiKey = apiKey, accountNumber = accountNumber)
            result.fold(
                onSuccess = { refresh() },
                onFailure = { throwable ->
                    // There is no retry for this case.
                    Logger.e(getString(resource = Res.string.account_error_update_credentials), throwable = throwable, tag = "AccountViewModel")
                    _uiState.update { currentUiState ->
                        currentUiState.updateUIForErrorAndStopLoading(message = getString(resource = Res.string.account_error_update_credentials))
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
                    Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
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
