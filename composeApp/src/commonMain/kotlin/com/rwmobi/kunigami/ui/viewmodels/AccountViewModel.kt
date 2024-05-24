/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.UpdateMeterPreferenceUseCase
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenLayout
import com.rwmobi.kunigami.ui.destinations.account.AccountUIState
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.extensions.generateRandomLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
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
        val requestedLayout = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> AccountScreenLayout.WideWrapped
            WindowWidthSizeClass.Medium -> AccountScreenLayout.Wide
            else -> AccountScreenLayout.Compact
        }

        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestedLayout = requestedLayout,
            )
        }
    }

    fun refresh() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch(dispatcher) {
            syncUserProfileUseCase().fold(
                onSuccess = { userProfile ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isDemoMode = false,
                            userProfile = userProfile,
                            isLoading = false,
                        )
                    }
                },
                onFailure = { throwable ->
                    if (throwable is IncompleteCredentialsException) {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isDemoMode = true,
                                isLoading = false,
                            )
                        }
                    } else {
                        updateUIForError(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
                        Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
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
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch {
            val result = initialiseAccountUseCase(apiKey = apiKey, accountNumber = accountNumber)

            result.fold(
                onSuccess = {
                    refresh()
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
                    Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
                },
            )
        }
    }

    fun updateMeterSerialNumber(
        mpan: String,
        meterSerialNumber: String,
    ) {
        viewModelScope.launch {
            val result = updateMeterPreferenceUseCase(mpan = mpan, meterSerialNumber = meterSerialNumber)

            result.fold(
                onSuccess = {
                    refresh()
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
                    Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
                },
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

    private fun updateUIForError(message: String) {
        _uiState.update { currentUiState ->
            val newErrorMessages = if (_uiState.value.errorMessages.any { it.message == message }) {
                currentUiState.errorMessages
            } else {
                currentUiState.errorMessages + ErrorMessage(
                    id = generateRandomLong(),
                    message = message,
                )
            }
            currentUiState.copy(
                isLoading = false,
                errorMessages = newErrorMessages,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("AccountViewModel", message = { "onCleared" })
    }
}
