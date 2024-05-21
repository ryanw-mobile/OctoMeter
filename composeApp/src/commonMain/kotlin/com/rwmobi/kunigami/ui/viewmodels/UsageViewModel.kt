/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.extensions.formatDate
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.usecase.GetConsumptionUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.model.BarChartData
import com.rwmobi.kunigami.ui.model.ConsumptionGroup
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.utils.generateRandomLong
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil

class UsageViewModel(
    private val octopusRepository: RestApiRepository,
    private val getConsumptionUseCase: GetConsumptionUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UsageUIState> = MutableStateFlow(UsageUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val usageColumnWidth = 175.dp

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

                        val consumptionGroup = consumptions
                            .groupBy { it.intervalStart.formatDate() }
                            .map { (date, items) -> ConsumptionGroup(title = date, consumptions = items) }

                        val verticalBarPlotEntries: List<VerticalBarPlotEntry<Int, Double>> = buildList {
                            consumptions.forEachIndexed { index, consumption ->
                                add(element = DefaultVerticalBarPlotEntry((index + 1), y = DefaultVerticalBarPosition(0.0, consumption.consumption)))
                            }
                        }

                        val labels: Map<Int, Int> = buildMap {
                            // Generate all possible labels
                            var lastRateValue: Int? = null
                            consumptions.forEachIndexed { index, consumption ->
                                val currentTime = consumption.intervalStart.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                                if (currentTime != lastRateValue && currentTime % 2 == 0) {
                                    put(index + 1, currentTime)
                                }
                                lastRateValue = currentTime
                            }
                        }

                        val toolTips = consumptions.map { consumption ->
                            "${consumption.intervalStart.toLocalHourMinuteString()} - ${consumption.intervalEnd.toLocalHourMinuteString()}\n${consumption.consumption} kWh"
                        }

                        currentUiState.copy(
                            isLoading = false,
                            consumptions = consumptionGroup,
                            consumptionRange = consumptionRange,
                            barChartData = BarChartData(
                                verticalBarPlotEntries = verticalBarPlotEntries,
                                labels = labels,
                                tooltips = toolTips,
                            ),
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
            Logger.v("UsageViewModel: ${screenSizeInfo.heightDp}h x ${screenSizeInfo.widthDp}w, isPortrait = ${screenSizeInfo.isPortrait()}")
            val requestedLayout = if (screenSizeInfo.isPortrait()) {
                RequestedChartLayout.Portrait
            } else {
                RequestedChartLayout.LandScape(
                    requestedMaxHeight = screenSizeInfo.heightDp * 2 / 3,
                )
            }

            val usageColumns = (screenSizeInfo.widthDp / usageColumnWidth).toInt()

            currentUiState.copy(
                requestedChartLayout = requestedLayout,
                requestedUsageColumns = usageColumns,
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
        Logger.v("UsageViewModel", message = { "onCleared" })
    }
}
