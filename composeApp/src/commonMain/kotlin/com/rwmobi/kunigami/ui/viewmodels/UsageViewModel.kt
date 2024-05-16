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
import com.rwmobi.kunigami.domain.usecase.GetConsumptionUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageScreenLayout
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.utils.generateRandomLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

class UsageViewModel(
    private val octopusRepository: RestApiRepository,
    private val getConsumptionUseCase: GetConsumptionUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UsageUIState> = MutableStateFlow(UsageUIState(isLoading = true))
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
            getConsumptionUseCase().fold(
                onSuccess = { consumptions ->
                    _uiState.update { currentUiState ->
                        val consumptionRange = if (consumptions.isEmpty()) {
                            0.0..0.0 // Return a default range if the list is empty
                        } else {
                            0.0..ceil(consumptions.maxOf { it.consumption } * 10) / 10.0
                        }

                        currentUiState.copy(
                            isLoading = false,
                            consumptions = consumptions,
                            consumptionRange = consumptionRange,
                        )
                    }
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                    Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                },
            )
        }
    }

    fun notifyScreenSizeChanged(screenSizeInfo: ScreenSizeInfo) {
        _uiState.update { currentUiState ->
            val requestedLayout = if (screenSizeInfo.isPortrait()) {
                UsageScreenLayout.Portrait
            } else {
                UsageScreenLayout.LandScape(
                    requestedMaxHeight = screenSizeInfo.heightDp * 2 / 3,
                )
            }

            currentUiState.copy(
                requestedLayout = requestedLayout,
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
        Logger.v("UsageViewModel", message = { "onCleared" })
    }
}
