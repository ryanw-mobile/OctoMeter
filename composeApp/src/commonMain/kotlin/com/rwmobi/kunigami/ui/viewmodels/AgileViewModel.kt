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
import com.rwmobi.kunigami.domain.usecase.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.ui.destinations.agile.AgileUIState
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.utils.generateRandomLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

class AgileViewModel(
    private val getStandardUnitRateUseCase: GetStandardUnitRateUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AgileUIState> = MutableStateFlow(AgileUIState(isLoading = true))
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
            getStandardUnitRateUseCase().fold(
                onSuccess = { rates ->
                    _uiState.update { currentUiState ->
                        val rateRange = if (rates.isEmpty()) {
                            0.0..0.0 // Return a default range if the list is empty
                        } else {
                            0.0..ceil(rates.maxOf { it.vatInclusivePrice } * 10) / 10.0
                        }

                        currentUiState.copy(
                            isLoading = false,
                            rates = rates,
                            rateRange = rateRange,
                        )
                    }
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: "Error when retrieving rates")
                    Logger.e("AgileViewModel", throwable = throwable, message = { "Error when retrieving rates" })
                },
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
        Logger.v("AgileViewModel", message = { "onCleared" })
    }
}
