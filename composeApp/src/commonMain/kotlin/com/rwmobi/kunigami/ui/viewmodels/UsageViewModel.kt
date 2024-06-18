/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.extensions.atEndOfDay
import com.rwmobi.kunigami.domain.extensions.atStartOfDay
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.model.consumption.getConsumptionRange
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.usecase.GenerateUsageInsightsUseCase
import com.rwmobi.kunigami.domain.usecase.GetConsumptionAndCostUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffSummaryUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionQueryFilter
import com.rwmobi.kunigami.ui.previewsampledata.FakeDemoUserProfile
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
    private val getTariffSummaryUseCase: GetTariffSummaryUseCase,
    private val getConsumptionAndCostUseCase: GetConsumptionAndCostUseCase,
    private val generateUsageInsightsUseCase: GenerateUsageInsightsUseCase,
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

    // TODO: need refactoring
    fun initialLoad() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            // Note that getUserProfile will provide a fake profile when isDemoMode == true
            val userProfile = getUserProfile()
            if (userProfile != null) {
                // Currently smart meter readings are not real-time. Yesterday's figures are the latest we can get.
                val referencePoint = Clock.System.now() - Duration.parse(value = "1d")

                val matchingTariffCode = userProfile.getSelectedElectricityMeterPoint()?.lookupAgreement(
                    referencePoint = referencePoint,
                )

                val tariffSummary = matchingTariffCode?.let {
                    getTariffSummaryUseCase(tariffCode = it.tariffCode).getOrNull()
                }
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        tariffSummary = tariffSummary,
                    )
                }

                var newConsumptionQueryFilter = ConsumptionQueryFilter(
                    presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
                    referencePoint = referencePoint,
                    requestedStart = referencePoint.atStartOfDay(),
                    requestedEnd = referencePoint.atEndOfDay(),
                )

                // UIState comes with a default presentationStyle. We try to go backward 5 times hoping for some valid results
                val accountMoveInDate = userProfile.account.movedInAt ?: Instant.DISTANT_PAST
                run loop@{
                    for (iteration in 0..3) {
                        getConsumptionAndCostUseCase(
                            periodFrom = newConsumptionQueryFilter.requestedStart,
                            periodTo = newConsumptionQueryFilter.requestedEnd,
                            groupBy = newConsumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
                        ).fold(
                            onSuccess = { consumptions ->
                                // During BST we expect 2 half-hour records returned for the last day
                                if (consumptions.size > 2) {
                                    propagateInsights(
                                        tariffSummary = tariffSummary,
                                        consumptionWithCost = consumptions,
                                    )
                                    propagateConsumptionsAndStopLoading(
                                        consumptionQueryFilter = newConsumptionQueryFilter,
                                        consumptionWithCost = consumptions,
                                    )
                                    return@loop // Done
                                } else {
                                    newConsumptionQueryFilter.navigateBackward(accountMoveInDate = accountMoveInDate)?.let {
                                        newConsumptionQueryFilter = it
                                    } ?: run {
                                        _uiState.update { currentUiState -> currentUiState.clearDataFieldsAndStopLoading() }
                                        return@loop // Can't go back. Give up with no data.
                                    }
                                }
                            },
                            onFailure = { throwable ->
                                Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                                _uiState.update { currentUiState ->
                                    currentUiState.filterErrorAndStopLoading(throwable = throwable, defaultMessage = "Error when retrieving consumptions")
                                }
                                return@loop // API Error. Give up
                            },
                        )
                    }
                }
            } else {
                // Not demo mode but couldn't get the user profile. We won't show anything
                _uiState.update { currentUiState -> currentUiState.clearDataFieldsAndStopLoading() }
            }
        }
    }

    // TODO: Needs a use-case when we handle more complex queries
    private suspend fun refresh(consumptionQueryFilter: ConsumptionQueryFilter) {
        startLoading()

        // Note that getUserProfile will provide a fake profile when isDemoMode == true
        val userProfile = getUserProfile()

        // If the query is over a period of time, it can have more than one tariffs
        val matchingTariffCodes = userProfile?.getSelectedElectricityMeterPoint()?.lookupAgreements(
            validFrom = consumptionQueryFilter.requestedStart,
            validTo = consumptionQueryFilter.requestedEnd,
        )

        // It is abnormal to expect we can get meter readings without any corresponding tariffs.
        // TODO: In ticket #137 we'll handle multiple tariffs (and multiple rates) in queries other than half-hourly
        if (matchingTariffCodes?.isNotEmpty() == true) {
            // TODO: Simplified handling by only considering one latest tariff for now
            val latestTariff = matchingTariffCodes.maxBy { agreement -> agreement.validTo }
            val tariffSummary = getTariffSummaryUseCase(tariffCode = latestTariff.tariffCode).getOrNull()
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    tariffSummary = tariffSummary,
                )
            }

            getConsumptionAndCostUseCase(
                periodFrom = consumptionQueryFilter.requestedStart,
                periodTo = consumptionQueryFilter.requestedEnd,
                groupBy = consumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
            ).fold(
                onSuccess = { consumptions ->
                    propagateInsights(
                        consumptionWithCost = consumptions,
                        tariffSummary = tariffSummary,
                    )
                    propagateConsumptionsAndStopLoading(
                        consumptionQueryFilter = consumptionQueryFilter,
                        consumptionWithCost = consumptions,
                    )
                },
                onFailure = { throwable ->
                    Logger.e("UsageViewModel", throwable = throwable, message = { "Error when retrieving consumptions" })
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable, defaultMessage = "Error when retrieving consumptions")
                    }
                },
            )
        }
    }

    fun onSwitchPresentationStyle(presentationStyle: ConsumptionPresentationStyle) {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value.consumptionQueryFilter) {
                val newConsumptionQueryFilter = ConsumptionQueryFilter(
                    presentationStyle = presentationStyle,
                    referencePoint = referencePoint,
                    requestedStart = ConsumptionQueryFilter.calculateStartDate(referencePoint = referencePoint, presentationStyle = presentationStyle),
                    requestedEnd = ConsumptionQueryFilter.calculateEndDate(referencePoint = referencePoint, presentationStyle = presentationStyle),
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
                _uiState.update { currentUiState ->
                    currentUiState.filterErrorAndStopLoading(throwable = IllegalArgumentException("Requested date is outside of the allowed range."))
                }
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
                _uiState.update { currentUiState ->
                    currentUiState.filterErrorAndStopLoading(throwable = IllegalArgumentException("Requested date is outside of the allowed range."))
                }
            } else {
                refresh(consumptionQueryFilter = consumptionQueryFilter)
            }
        }
    }

    fun notifyScreenSizeChanged(screenSizeInfo: ScreenSizeInfo, windowSizeClass: WindowSizeClass) {
        _uiState.update { currentUiState ->
            currentUiState.updateLayoutType(screenSizeInfo = screenSizeInfo, windowSizeClass = windowSizeClass)
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

    /***
     * IMPORTANT: For Demo purpose, when IncompleteCredentialsException is caught,
     * we inject a Faked UserProfile on the fly only for this screen.
     * Leave UserPreferences alone, so it will always turn on isDemoMode for the rest of the App.
     */
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
                    val fakeDemoUserProfile = FakeDemoUserProfile.flexibleOctopusRegionADirectDebit
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isDemoMode = true,
                            userProfile = fakeDemoUserProfile,
                        )
                    }
                    return fakeDemoUserProfile
                } else {
                    Logger.e(getString(resource = Res.string.account_error_load_account), throwable = throwable, tag = "AccountViewModel")
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable)
                    }
                    return null
                }
            },
        )
    }

    private fun propagateInsights(
        tariffSummary: TariffSummary?,
        consumptionWithCost: List<ConsumptionWithCost>?,
    ) {
        val insights = generateUsageInsightsUseCase(
            tariffSummary = tariffSummary,
            consumptionWithCost = consumptionWithCost,
        )
        _uiState.update { currentUiState ->
            currentUiState.copy(
                insights = insights,
            )
        }
    }

    private suspend fun propagateConsumptionsAndStopLoading(
        consumptionQueryFilter: ConsumptionQueryFilter,
        consumptionWithCost: List<ConsumptionWithCost>,
    ) {
        val consumptions = consumptionWithCost.map { it.consumption }
        val consumptionRange = consumptions.getConsumptionRange()
        val labels = consumptionQueryFilter.generateChartLabels(consumptions = consumptions)
        val consumptionGroupedCells = consumptionQueryFilter.groupChartCells(consumptions = consumptions)
        val toolTips = consumptionQueryFilter.generateChartToolTips(consumptions = consumptions)
        val verticalBarPlotEntries: List<VerticalBarPlotEntry<Int, Double>> = buildList {
            consumptions.forEachIndexed { index, consumption ->
                add(
                    element = DefaultVerticalBarPlotEntry(
                        x = index,
                        y = DefaultVerticalBarPosition(yMin = 0.0, yMax = consumption.kWhConsumed),
                    ),
                )
            }
        }

        _uiState.update { currentUiState ->
            currentUiState.copy(
                consumptionQueryFilter = consumptionQueryFilter,
                consumptionGroupedCells = consumptionGroupedCells,
                consumptionRange = consumptionRange,
                barChartData = BarChartData(
                    verticalBarPlotEntries = verticalBarPlotEntries,
                    labels = labels,
                    tooltips = toolTips,
                ),
                isLoading = false,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("UsageViewModel", message = { "onCleared" })
    }
}
