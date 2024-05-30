/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.extensions.roundToDayEnd
import com.rwmobi.kunigami.domain.extensions.roundToDayStart
import com.rwmobi.kunigami.domain.extensions.roundToNearestEvenHundredth
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.getConsumptionTimeSpan
import com.rwmobi.kunigami.domain.model.consumption.getRange
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.usecase.GetConsumptionUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.extensions.generateRandomLong
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionQueryFilter
import com.rwmobi.kunigami.ui.model.consumption.Insights
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
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration

class UsageViewModel(
    private val syncUserProfileUseCase: SyncUserProfileUseCase,
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
        startLoading()

        viewModelScope.launch(dispatcher) {
            val userProfile = getUserProfile()

            if (_uiState.value.isDemoMode == true) {
                // TODO: Call isolated fake data generator
            } else if (userProfile != null) {
                // Currently smart meter readings are not real-time. Yesterday's figures are the latest we can get.
                val pointOfReference = Clock.System.now() - Duration.parse(value = "1d")
                var newConsumptionQueryFilter = ConsumptionQueryFilter(
                    presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
                    pointOfReference = pointOfReference,
                    requestedStart = pointOfReference.roundToDayStart(),
                    requestedEnd = pointOfReference.roundToDayEnd(),
                )

                // UIState comes with a default presentationStyle. We try to go backward 5 times hoping for some valid results
                val accountMoveInDate = userProfile.account?.movedInAt ?: Instant.DISTANT_PAST
                run loop@{
                    for (iteration in 0..3) {
                        getConsumptionUseCase(
                            periodFrom = newConsumptionQueryFilter.requestedStart,
                            periodTo = newConsumptionQueryFilter.requestedEnd,
                            groupBy = newConsumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
                        ).fold(
                            onSuccess = { consumptions ->
                                // During BST we expect 2 half-hour records returned for the last day
                                if (consumptions.size > 2) {
                                    propagateInsights(
                                        tariffSummary = userProfile.tariffSummary,
                                        consumptions = consumptions,
                                    )
                                    propagateConsumptions(
                                        consumptionQueryFilter = newConsumptionQueryFilter,
                                        consumptions = consumptions,
                                    )
                                    return@loop // Done
                                } else {
                                    newConsumptionQueryFilter.navigateBackward(accountMoveInDate = accountMoveInDate)?.let {
                                        newConsumptionQueryFilter = it
                                    } ?: run {
                                        clearDataFields()
                                        return@loop // Can't go back. Give up with no data.
                                    }
                                }
                            },
                            onFailure = { throwable ->
                                updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
                                Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                                return@loop // API Error. Give up
                            },
                        )
                    }
                }
            } else {
                // Not demo mode but couldn't get the user profile. We won't show anything
                clearDataFields()
            }

            stopLoading()
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
            val accountMoveInDate = _uiState.value.userProfile?.account?.movedInAt ?: Instant.DISTANT_PAST
            val consumptionQueryFilter = _uiState.value.consumptionQueryFilter.navigateBackward(accountMoveInDate = accountMoveInDate)
            if (consumptionQueryFilter == null) {
                Logger.e("UsageViewModel", message = { "onNavigateForward request declined." })
                updateUIForError(message = "Requested date is outside of the allowed range.")
                stopLoading()
            } else {
                refresh(consumptionQueryFilter = consumptionQueryFilter)
            }
        }
    }

    fun onNextTimeFrame() {
        viewModelScope.launch(dispatcher) {
            val consumptionQueryFilter = _uiState.value.consumptionQueryFilter.navigateForward()
            if (consumptionQueryFilter == null) {
                Logger.e("UsageViewModel", message = { "onNavigateForward request declined." })
                updateUIForError(message = "Requested date is outside of the allowed range.")
                stopLoading()
            } else {
                refresh(consumptionQueryFilter = consumptionQueryFilter)
            }
        }
    }

    fun notifyScreenSizeChanged(screenSizeInfo: ScreenSizeInfo, windowSizeClass: WindowSizeClass) {
        _uiState.update { currentUiState ->
            Logger.v("UsageViewModel: $windowSizeClass, ${screenSizeInfo.heightDp}h x ${screenSizeInfo.widthDp}w, isPortrait = ${screenSizeInfo.isPortrait()}")
            val requestedLayout = if (screenSizeInfo.isPortrait()) {
                RequestedChartLayout.Portrait
            } else {
                RequestedChartLayout.LandScape(
                    requestedMaxHeight = screenSizeInfo.heightDp / 2,
                )
            }

            val usageColumns = (screenSizeInfo.widthDp / usageColumnWidth).toInt()

            currentUiState.copy(
                requestedAdaptiveLayout = windowSizeClass.widthSizeClass,
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

    private fun startLoading() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }
    }

    private fun stopLoading() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
            )
        }
    }

    private suspend fun refresh(consumptionQueryFilter: ConsumptionQueryFilter) {
        startLoading()
        val userProfile = getUserProfile()
        getConsumptionUseCase(
            periodFrom = consumptionQueryFilter.requestedStart,
            periodTo = consumptionQueryFilter.requestedEnd,
            groupBy = consumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
        ).fold(
            onSuccess = { consumptions ->
                propagateInsights(
                    consumptions = consumptions,
                    tariffSummary = userProfile?.tariffSummary,
                )
                propagateConsumptions(
                    consumptionQueryFilter = consumptionQueryFilter,
                    consumptions = consumptions,
                )
            },
            onFailure = { throwable ->
                Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                updateUIForError(message = throwable.message ?: "Error when retrieving consumptions")
            },
        )
        stopLoading()
    }

    private fun clearDataFields() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                userProfile = null,
                consumptionGroupedCells = listOf(),
                consumptionRange = 0.0..0.0,
                barChartData = null,
                insights = null,
            )
        }
    }

    private suspend fun getUserProfile(): UserProfile? {
        syncUserProfileUseCase().fold(
            onSuccess = { userProfile ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isDemoMode = false,
                        userProfile = userProfile,
                    )
                }
                return userProfile
            },
            onFailure = { throwable ->
                if (throwable is IncompleteCredentialsException) {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isDemoMode = true,
                        )
                    }
                } else {
                    Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
                    updateUIForError(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
                    stopLoading()
                }
            },
        )
        return null
    }

    private fun propagateInsights(
        tariffSummary: TariffSummary?,
        consumptions: List<Consumption>?,
    ): Insights? {
        val insights = if (tariffSummary == null || consumptions.isNullOrEmpty()) {
            null
        } else {
            val consumptionAggregateRounded = consumptions.sumOf { it.consumption }.roundToNearestEvenHundredth()
            val consumptionTimeSpan = consumptions.getConsumptionTimeSpan()
            val roughCost = ((consumptionTimeSpan * tariffSummary.vatInclusiveStandingCharge) + (consumptionAggregateRounded * tariffSummary.vatInclusiveUnitRate)) / 100.0
            val consumptionDailyAverage = (consumptions.sumOf { it.consumption } / consumptions.getConsumptionTimeSpan()).roundToNearestEvenHundredth()
            val costDailyAverage = (tariffSummary.vatInclusiveStandingCharge + consumptionDailyAverage * tariffSummary.vatInclusiveUnitRate) / 100.0
            val consumptionAnnualProjection = (consumptions.sumOf { it.consumption } / consumptionTimeSpan * 365.25).roundToNearestEvenHundredth()
            val costAnnualProjection = (tariffSummary.vatInclusiveStandingCharge * 365.25 + consumptionAnnualProjection * tariffSummary.vatInclusiveUnitRate) / 100.0

            Insights(
                consumptionAggregateRounded = consumptionAggregateRounded,
                consumptionTimeSpan = consumptionTimeSpan,
                roughCost = roughCost,
                consumptionDailyAverage = consumptionDailyAverage,
                costDailyAverage = costDailyAverage,
                consumptionAnnualProjection = consumptionAnnualProjection,
                costAnnualProjection = costAnnualProjection,
            )
        }

        _uiState.update { currentUiState ->
            currentUiState.copy(
                insights = insights,
            )
        }

        return insights
    }

    private suspend fun propagateConsumptions(
        consumptionQueryFilter: ConsumptionQueryFilter,
        consumptions: List<Consumption>,
    ) {
        _uiState.update { currentUiState ->
            val consumptionRange = consumptions.getRange()
            val labels = consumptionQueryFilter.generateChartLabels(consumptions = consumptions)
            val consumptionGroupedCells = consumptionQueryFilter.groupChartCells(consumptions = consumptions)
            val toolTips = consumptionQueryFilter.generateChartToolTips(consumptions = consumptions)
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

            currentUiState.copy(
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
                errorMessages = newErrorMessages,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("UsageViewModel", message = { "onCleared" })
    }
}
