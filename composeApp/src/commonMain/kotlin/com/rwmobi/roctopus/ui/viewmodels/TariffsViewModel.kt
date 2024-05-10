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
}
