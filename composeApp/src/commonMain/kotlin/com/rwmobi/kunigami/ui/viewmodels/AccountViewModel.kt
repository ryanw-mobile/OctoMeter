/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.usecase.GetTariffRatesUseCase
import com.rwmobi.kunigami.domain.usecase.GetUserAccountUseCase
import com.rwmobi.kunigami.ui.destinations.account.AccountUIState
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.utils.generateRandomLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val octopusRepository: RestApiRepository,
    private val getUserAccountUseCase: GetUserAccountUseCase,
    private val getTariffRatesUseCase: GetTariffRatesUseCase,
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

    fun refresh() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch(dispatcher) {
            val userAccount = getUserAccountUseCase()
            userAccount.fold(
                onSuccess = { account ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            account = account,
                        )
                    }
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: "Error when retrieving tariffs")
                    Logger.e("AccountViewModel", throwable = throwable, message = { "Error when retrieving tariffs" })
                },
            )

            val tariffCode = _uiState.value.account?.electricityMeterPoints?.get(0)?.currentAgreement?.tariffCode ?: return@launch

            val tariffRates = getTariffRatesUseCase(
                productCode = extractSegment(tariffCode) ?: "",
                tariffCode = tariffCode,
            )
            tariffRates.fold(
                onSuccess = { tariff ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isDemoMode = false,
                            tariff = tariff,
                            isLoading = false,
                        )
                    }
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: "Error when retrieving tariffs")
                    Logger.e("AccountViewModel", throwable = throwable, message = { "Error when retrieving tariffs" })
                },
            )
        }
    }

    fun clearCredential() {
        // TODO: call use-case
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

    private fun extractSegment(input: String): String? {
        val parts = input.split("-")
        // Check if there are enough parts to remove
        if (parts.size > 3) {
            // Exclude the first two and the last segments
            val relevantParts = parts.drop(2).dropLast(1)
            return relevantParts.joinToString("-")
        }
        return null
    }
}
