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

    suspend fun loadPreferredWindowSize(): DpSize = withContext(dispatcher) {
        userPreferencesRepository.getWindowSize() ?: DpSize(width = 800.dp, height = 560.dp)
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
