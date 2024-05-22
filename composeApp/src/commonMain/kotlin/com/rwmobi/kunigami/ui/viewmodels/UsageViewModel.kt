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
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.usecase.GetConsumptionUseCase
import com.rwmobi.kunigami.domain.usecase.GetUserAccountUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.model.BarChartData
import com.rwmobi.kunigami.ui.model.ConsumptionGroupedCells
import com.rwmobi.kunigami.ui.model.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.model.ConsumptionQueryFilter
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
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
import org.jetbrains.compose.resources.getString
import kotlin.math.ceil

class UsageViewModel(
    private val octopusRepository: RestApiRepository,
    private val getConsumptionUseCase: GetConsumptionUseCase,
    private val getUserAccountUseCase: GetUserAccountUseCase,
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

        // Default style is set in the UIState. We try to go backward 5 times hoping for some valid results
        viewModelScope.launch(dispatcher) {
            getUserAccountUseCase().fold(
                onSuccess = { account ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isDemoMode = false,
                            account = account,
                        )
                    }
                },
                onFailure = { throwable ->
                    if (throwable is IncompleteCredentialsException) {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isDemoMode = true,
                                // TODO: supply a demo account instance
                            )
                        }
                    } else {
                        updateUIForError(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
                        Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
                        return@launch
                    }
                },
            )

            val accountMoveInDate = _uiState.value.account?.movedInAt ?: Instant.DISTANT_PAST
            var newConsumptionQueryFilter = _uiState.value.consumptionQueryFilter.copy()
            (5 downTo 1).forEach { _ ->
                getConsumptionUseCase(
                    periodFrom = newConsumptionQueryFilter.requestedStart,
                    periodTo = newConsumptionQueryFilter.requestedEnd,
                    groupBy = newConsumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
                ).fold(
                    onSuccess = { consumptions ->
                        if (consumptions.size > 4) {
                            processConsumptions(
                                consumptionQueryFilter = newConsumptionQueryFilter,
                                consumptions = consumptions,
                            )
                            return@launch
                        } else {
                            newConsumptionQueryFilter.navigateBackward(accountMoveInDate = accountMoveInDate)?.let {
                                newConsumptionQueryFilter = it
                            } ?: run {
                                _uiState.update { currentUiState ->
                                    currentUiState.copy(
                                        isLoading = false,
                                    )
                                }
                                return@launch // No data
                            }
                        }
                    },
                    onFailure = { throwable ->
                        updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                        Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                        return@launch
                    },
                )
            }
        }
    }

    fun onSwitchPresentationStyle(presentationStyle: ConsumptionPresentationStyle) {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value.consumptionQueryFilter) {
                val newConsumptionQueryFilter = ConsumptionQueryFilter(
                    presentationStyle = presentationStyle,
                    pointOfReference = pointOfReference,
                    requestedStart = ConsumptionQueryFilter.calculateStartDate(pointOfReference = pointOfReference, presentationStyle = presentationStyle),
                    requestedEnd = ConsumptionQueryFilter.calculateEndDate(pointOfReference = pointOfReference, presentationStyle = presentationStyle),
                )

                refresh(consumptionQueryFilter = newConsumptionQueryFilter)
            }
        }
    }

    fun onPreviousTimeFrame() {
        viewModelScope.launch(dispatcher) {
            val accountMoveInDate = _uiState.value.account?.movedInAt ?: Instant.DISTANT_PAST
            val consumptionQueryFilter = _uiState.value.consumptionQueryFilter.navigateBackward(accountMoveInDate = accountMoveInDate)
            if (consumptionQueryFilter == null) {
                updateUIForError(message = "Requested date is outside of the allowed range.")
                Logger.e("UsageViewModel", message = { "onNavigateForward request declined." })
            } else {
                refresh(consumptionQueryFilter = consumptionQueryFilter)
            }
        }
    }

    fun onNextTimeFrame() {
        viewModelScope.launch(dispatcher) {
            val consumptionQueryFilter = _uiState.value.consumptionQueryFilter.navigateForward()
            if (consumptionQueryFilter == null) {
                updateUIForError(message = "Requested date is outside of the allowed range.")
                Logger.e("UsageViewModel", message = { "onNavigateForward request declined." })
            } else {
                refresh(consumptionQueryFilter = consumptionQueryFilter)
            }
        }
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

    fun requestScrollToTop(enabled: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestScrollToTop = enabled,
            )
        }
    }

    private suspend fun refresh(consumptionQueryFilter: ConsumptionQueryFilter) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        getConsumptionUseCase(
            periodFrom = consumptionQueryFilter.requestedStart,
            periodTo = consumptionQueryFilter.requestedEnd,
            groupBy = consumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
        ).fold(
            onSuccess = { consumptions ->
                processConsumptions(
                    consumptionQueryFilter = consumptionQueryFilter,
                    consumptions = consumptions,
                )
            },
            onFailure = { throwable ->
                updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
            },
        )
    }

    private fun processConsumptions(
        consumptionQueryFilter: ConsumptionQueryFilter,
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
                consumptionQueryFilter = consumptionQueryFilter,
                consumptionGroupedCells = consumptionGroupedCells,
                consumptionRange = consumptionRange,
                barChartData = BarChartData(
                    verticalBarPlotEntries = verticalBarPlotEntries,
                    labels = labels,
                    tooltips = toolTips,
                ),
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
