/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlatformMainViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private var _windowSize = MutableStateFlow(DpSize(800.dp, 560.dp))
    val windowSize = _windowSize.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            _windowSize.value = loadPreferredWindowSize()
        }
    }

    suspend fun loadPreferredWindowSize(): DpSize {
        return withContext(dispatcher) {
            userPreferencesRepository.getWindowSize() ?: DpSize(width = 800.dp, height = 560.dp)
        }
    }

    fun cachePreferredWindowSize(size: DpSize) {
        viewModelScope.launch(dispatcher) {
            userPreferencesRepository.setWindowSize(size = size)
            _windowSize.value = size
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("PlatformMainViewModel", message = { "onCleared" })
    }
}
