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
import com.rwmobi.kunigami.domain.extensions.roundDownToDay
import com.rwmobi.kunigami.domain.extensions.roundUpToDayEnd
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionGrouping
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.usecase.GetConsumptionUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.model.BarChartData
import com.rwmobi.kunigami.ui.model.ConsumptionGroupedCells
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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.time.Duration

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

    fun initialLoad() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        // Currently we don't get real time meter readings, best would be yesterday or the day before
        // We try to loop for 5 days
        viewModelScope.launch(dispatcher) {
            var pointOfReference = Clock.System.now()
            val grouping = ConsumptionGrouping.HALF_HOURLY
            var remainingIteration = 5

            do {
                pointOfReference = pointOfReference.minus(duration = Duration.parse(value = "1d"))
                val periodFrom = pointOfReference.roundDownToDay()
                val periodTo = pointOfReference.roundUpToDayEnd()

                getConsumptionUseCase(
                    periodFrom = periodFrom,
                    periodTo = periodTo,
                    groupBy = grouping,
                ).fold(
                    onSuccess = { consumptions ->
                        if (consumptions.size < 12) {
                            remainingIteration = remainingIteration--
                        } else {
                            processConsumptions(
                                grouping = grouping,
                                pointOfReference = pointOfReference,
                                requestedStart = periodFrom,
                                requestedEnd = periodTo,
                                consumptions = consumptions,
                            )
                            remainingIteration = 0
                        }
                    },
                    onFailure = { throwable ->
                        updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                        Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                        remainingIteration = 0
                    },
                )
            } while (remainingIteration > 0)
        }
    }

    private fun processConsumptions(
        grouping: ConsumptionGrouping,
        pointOfReference: Instant,
        requestedStart: Instant,
        requestedEnd: Instant,
        consumptions: List<Consumption>,
    ) {
        _uiState.update { currentUiState ->
            val consumptionRange = if (consumptions.isEmpty()) {
                0.0..0.0 // Return a default range if the list is empty
            } else {
                0.0..ceil(consumptions.maxOf { it.consumption } * 10) / 10.0
            }

            val consumptionGroupedCells = consumptions
                .groupBy { it.intervalStart.toLocalDateString() }
                .map { (date, items) -> ConsumptionGroupedCells(title = date, consumptions = items) }

            val verticalBarPlotEntries: List<VerticalBarPlotEntry<Int, Double>> = buildList {
                consumptions.forEachIndexed { index, consumption ->
                    add(
                        element = DefaultVerticalBarPlotEntry(
                            x = index,
                            y = DefaultVerticalBarPosition(yMin = 0.0, yMax = consumption.consumption),
                        ),
                    )
                }
            }

            val labels: Map<Int, String> = buildMap {
                // Generate all possible labels
                var lastRateValue: Int? = null
                consumptions.forEachIndexed { index, consumption ->
                    val currentTime = consumption.intervalStart.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                    if (currentTime != lastRateValue && currentTime % 2 == 0) {
                        put(key = index, value = currentTime.toString().padStart(2, '0'))
                    }
                    lastRateValue = currentTime
                }
            }

            val toolTips = consumptions.map { consumption ->
                "${consumption.intervalStart.toLocalHourMinuteString()} - ${consumption.intervalEnd.toLocalHourMinuteString()}\n${consumption.consumption} kWh"
            }

            currentUiState.copy(
                isLoading = false,
                grouping = grouping,
                pointOfReference = pointOfReference,
                requestedStart = requestedStart,
                requestedEnd = requestedEnd,
                consumptionGroupedCells = consumptionGroupedCells,
                consumptionRange = consumptionRange,
                canNavigateForward = canNavigateForward(
                    pointOfReference = pointOfReference,
                    consumptionGrouping = grouping,
                ),
                canNavigateBack = true,
                barChartData = BarChartData(
                    verticalBarPlotEntries = verticalBarPlotEntries,
                    labels = labels,
                    tooltips = toolTips,
                ),
            )
        }
    }

    fun refresh() {
//        _uiState.update { currentUiState ->
//            currentUiState.copy(
//                isLoading = true,
//            )
//        }
//
//        viewModelScope.launch(dispatcher) {
//            getConsumptionUseCase(
//                periodReference = Clock.System.now().minus(duration = Duration.parse("7d")).roundDownToDay(),
//                groupBy = ConsumptionGrouping.WEEK,
//            ).fold(
//                onSuccess = { consumptions ->
//                    processConsumptions(consumptions = consumptions)
//                },
//                onFailure = { throwable ->
//                    updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
//                    Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
//                },
//            )
//        }
    }

    fun notifyScreenSizeChanged(screenSizeInfo: ScreenSizeInfo) {
        _uiState.update { currentUiState ->
            Logger.v("UsageViewModel: ${screenSizeInfo.heightDp}h x ${screenSizeInfo.widthDp}w, isPortrait = ${screenSizeInfo.isPortrait()}")
            val requestedLayout = if (screenSizeInfo.isPortrait()) {
                RequestedChartLayout.Portrait
            } else {
                RequestedChartLayout.LandScape(
                    requestedMaxHeight = screenSizeInfo.heightDp / 2,
                )
            }

            val usageColumns = (screenSizeInfo.widthDp / usageColumnWidth).toInt()

            currentUiState.copy(
                requestedChartLayout = requestedLayout,
                requestedUsageColumns = usageColumns,
            )
        }
    }

    fun onNavigateBack() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        // Currently we don't get real time meter readings, best would be yesterday or the day before
        // We try to loop for 5 days
        viewModelScope.launch(dispatcher) {
            var pointOfReference = _uiState.value.pointOfReference
            var periodFrom = pointOfReference.roundDownToDay()
            var periodTo = pointOfReference.roundUpToDayEnd()
            val grouping = _uiState.value.grouping

            when (grouping) {
                ConsumptionGrouping.HALF_HOURLY -> {
                    // minus 1 day
                    pointOfReference = pointOfReference.minus(duration = Duration.parse("1d"))
                    periodFrom = pointOfReference.roundDownToDay()
                    periodTo = pointOfReference.roundUpToDayEnd()
                }

                ConsumptionGrouping.DAY -> {
                    // minus 1 month?
                }

                ConsumptionGrouping.WEEK -> {
                    // minus 1 week?
                }

                ConsumptionGrouping.MONTH -> {
                }

                ConsumptionGrouping.QUARTER -> {
                }
            }

            getConsumptionUseCase(
                periodFrom = periodFrom,
                periodTo = periodTo,
                groupBy = _uiState.value.grouping,
            ).fold(
                onSuccess = { consumptions ->
                    processConsumptions(
                        grouping = grouping,
                        pointOfReference = pointOfReference,
                        requestedStart = periodFrom,
                        requestedEnd = periodTo,
                        consumptions = consumptions,
                    )
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                    Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                },
            )
        }
    }

    fun onNavigateForward() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        // Currently we don't get real time meter readings, best would be yesterday or the day before
        // We try to loop for 5 days
        viewModelScope.launch(dispatcher) {
            var pointOfReference = _uiState.value.pointOfReference
            var periodFrom = pointOfReference.roundDownToDay()
            var periodTo = pointOfReference.roundUpToDayEnd()
            val grouping = _uiState.value.grouping

            when (grouping) {
                ConsumptionGrouping.HALF_HOURLY -> {
                    // minus 1 day
                    pointOfReference = pointOfReference.plus(duration = Duration.parse("1d"))
                    periodFrom = pointOfReference.roundDownToDay()
                    periodTo = pointOfReference.roundUpToDayEnd()
                }

                ConsumptionGrouping.DAY -> {
                    // minus 1 month?
                }

                ConsumptionGrouping.WEEK -> {
                    // minus 1 week?
                }

                ConsumptionGrouping.MONTH -> {
                }

                ConsumptionGrouping.QUARTER -> {
                }
            }

            getConsumptionUseCase(
                periodFrom = periodFrom,
                periodTo = periodTo,
                groupBy = _uiState.value.grouping,
            ).fold(
                onSuccess = { consumptions ->
                    processConsumptions(
                        grouping = grouping,
                        pointOfReference = pointOfReference,
                        requestedStart = periodFrom,
                        requestedEnd = periodTo,
                        consumptions = consumptions,
                    )
                },
                onFailure = { throwable ->
                    updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                    Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
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

    private fun canNavigateForward(
        pointOfReference: Instant,
        consumptionGrouping: ConsumptionGrouping,
    ): Boolean {
        val now = Clock.System.now()

        // Tentative as obviously ConsumptionGrouping Needs Revising
        val newPointOfReference = when (consumptionGrouping) {
            ConsumptionGrouping.HALF_HOURLY -> {
                // next day
                pointOfReference.plus(duration = Duration.parse("1d"))
            }

            ConsumptionGrouping.DAY -> {
                pointOfReference.plus(duration = Duration.parse("1d"))
            }

            ConsumptionGrouping.WEEK -> {
                pointOfReference.plus(duration = Duration.parse("1w"))
            }

            ConsumptionGrouping.MONTH -> {
                pointOfReference.plus(duration = Duration.parse("30d"))
            }

            ConsumptionGrouping.QUARTER -> {
                pointOfReference.plus(duration = Duration.parse("90d"))
            }
        }

        return newPointOfReference < now
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
