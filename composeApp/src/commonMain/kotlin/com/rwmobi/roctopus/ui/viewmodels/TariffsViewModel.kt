/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.roctopus.domain.repository.OctopusRepository
import com.rwmobi.roctopus.ui.destinations.tariffs.TariffsUIState
import com.rwmobi.roctopus.ui.model.ErrorMessage
import com.rwmobi.roctopus.ui.utils.generateRandomLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TariffsViewModel(
    private val octopusRepository: OctopusRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TariffsUIState> = MutableStateFlow(TariffsUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun refresh() {
        viewModelScope.launch(dispatcher) {
            val products = octopusRepository.getProducts().getOrNull()
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = false,
                    products = products ?: emptyList(),
                )
            }
        }
    }

    private fun updateUIForError(message: String) {
        if (_uiState.value.errorMessages.filter { it.message == message }.isNotEmpty()) {
            return
        }

        val newErrorMessage = ErrorMessage(
            id = generateRandomLong(),
            message = message,
        )
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                errorMessages = currentUiState.errorMessages + newErrorMessage,
            )
        }
    }
}
