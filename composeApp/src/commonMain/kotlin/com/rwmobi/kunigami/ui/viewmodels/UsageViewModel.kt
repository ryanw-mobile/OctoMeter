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
import com.rwmobi.kunigami.domain.extensions.getDayRange
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.model.consumption.getConsumptionRange
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.usecase.GenerateUsageInsightsUseCase
import com.rwmobi.kunigami.domain.usecase.GetConsumptionAndCostUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.ui.destinations.usage.UsageScreenType
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionQueryFilter
import com.rwmobi.kunigami.ui.previewsampledata.FakeDemoUserProfile
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
import kunigami.composeapp.generated.resources.fallback_error_no_consumptions
import kunigami.composeapp.generated.resources.usage_error_date_out_of_range
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration

class UsageViewModel(
    private val syncUserProfileUseCase: SyncUserProfileUseCase,
    private val getTariffUseCase: GetTariffUseCase,
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

    fun initialLoad() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            // Note that getUserProfile will provide a fake profile when isDemoMode == true
            val userProfile = getUserProfile()
            if (userProfile != null) {
                // Currently smart meter readings are not real-time. Yesterday's figures are the latest we can get.
                val referencePoint = Clock.System.now() - Duration.parse(value = "1d")
                val matchingTariffCode = userProfile.getSelectedElectricityMeterPoint()?.lookupAgreement(referencePoint = referencePoint)
                val tariffSummary = matchingTariffCode?.let {
                    getTariffUseCase(tariffCode = it.tariffCode).getOrNull()
                }

                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        tariff = tariffSummary,
                    )
                }

                var newConsumptionQueryFilter = ConsumptionQueryFilter(
                    presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
                    referencePoint = referencePoint,
                    requestedPeriod = referencePoint.getDayRange(),
                )

                // UIState comes with a default presentationStyle. We try to go backward 3 times hoping for some valid results
                val firstTariffStartDate = _uiState.value.userProfile?.getSelectedElectricityMeterPoint()?.getFirstTariffStartDate() ?: Instant.DISTANT_FUTURE
                var remainingRetryAttempt = 4
                do {
                    getConsumptionAndCostUseCase(
                        period = newConsumptionQueryFilter.requestedPeriod,
                        groupBy = newConsumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
                    ).fold(
                        onSuccess = { consumptions ->
                            // During BST we expect 2 half-hour records returned for the last day
                            if (consumptions.size > 2) {
                                propagateInsightConsumptionsAndStopLoading(
                                    consumptionQueryFilter = newConsumptionQueryFilter,
                                    consumptionWithCost = consumptions,
                                    tariff = tariffSummary,
                                )
                                remainingRetryAttempt = 0 // Done
                            } else {
                                newConsumptionQueryFilter.navigateBackward(firstTariffStartDate = firstTariffStartDate)?.let {
                                    newConsumptionQueryFilter = it
                                    remainingRetryAttempt -= 1 // Retry
                                } ?: run {
                                    _uiState.update { currentUiState -> currentUiState.clearDataFieldsAndStopLoading() }
                                    remainingRetryAttempt = 0 // Can't go back. Give up with no data.
                                }
                            }
                        },
                        onFailure = { throwable ->
                            val fallbackErrorMessage = getString(resource = Res.string.fallback_error_no_consumptions)
                            Logger.e("UsageViewModel", throwable = throwable, message = { fallbackErrorMessage })
                            _uiState.update { currentUiState ->
                                currentUiState.filterErrorAndStopLoading(throwable = throwable, defaultMessage = fallbackErrorMessage)
                            }
                            remainingRetryAttempt = 0 // API Error. Give up
                        },
                    )
                } while (remainingRetryAttempt > 0)
            } else {
                // Not demo mode but couldn't get the user profile. We won't show anything
                _uiState.update { currentUiState -> currentUiState.clearDataFieldsAndStopLoading() }
            }
        }
    }

    private suspend fun refresh(consumptionQueryFilter: ConsumptionQueryFilter) {
        startLoading()

        // Note that getUserProfile will provide a fake profile when isDemoMode == true
        val userProfile = getUserProfile()

        // If the query is over a period of time, it can have more than one tariffs
        val matchingTariffCodes = userProfile?.getSelectedElectricityMeterPoint()?.lookupAgreements(
            period = consumptionQueryFilter.requestedPeriod,
        )

        // It is abnormal to expect we can get meter readings without any corresponding tariffs.
        // TODO: In ticket #137 we'll handle multiple tariffs (and multiple rates) in queries other than half-hourly
        if (matchingTariffCodes?.isNotEmpty() == true) {
            // TODO: Simplified handling by only considering one latest tariff for now
            val latestTariff = matchingTariffCodes.maxBy { agreement -> agreement.period.endInclusive }
            val tariffSummary = getTariffUseCase(tariffCode = latestTariff.tariffCode).getOrNull()
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    tariff = tariffSummary,
                )
            }

            getConsumptionAndCostUseCase(
                period = consumptionQueryFilter.requestedPeriod,
                groupBy = consumptionQueryFilter.presentationStyle.getConsumptionDataGroup(),
            ).fold(
                onSuccess = { consumptions ->
                    propagateInsightConsumptionsAndStopLoading(
                        consumptionQueryFilter = consumptionQueryFilter,
                        consumptionWithCost = consumptions,
                        tariff = tariffSummary,
                    )
                },
                onFailure = { throwable ->
                    val fallbackErrorMessage = getString(resource = Res.string.fallback_error_no_consumptions)
                    Logger.e("UsageViewModel", throwable = throwable, message = { fallbackErrorMessage })
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable, defaultMessage = getString(resource = Res.string.fallback_error_no_consumptions))
                    }
                },
            )
        } else {
            _uiState.update { currentUiState ->
                val errorMessage = getString(resource = Res.string.fallback_error_no_consumptions)
                currentUiState.filterErrorAndStopLoading(throwable = IllegalStateException(errorMessage))
            }
        }
    }

    fun onSwitchPresentationStyle(
        consumptionQueryFilter: ConsumptionQueryFilter,
        presentationStyle: ConsumptionPresentationStyle,
    ) {
        viewModelScope.launch(dispatcher) {
            with(consumptionQueryFilter) {
                val newConsumptionQueryFilter = ConsumptionQueryFilter(
                    presentationStyle = presentationStyle,
                    referencePoint = referencePoint,
                    requestedPeriod = ConsumptionQueryFilter.calculateQueryPeriod(
                        referencePoint = referencePoint,
                        presentationStyle = presentationStyle,
                    ),
                )

                refresh(consumptionQueryFilter = newConsumptionQueryFilter)
            }
        }
    }

    fun onPreviousTimeFrame(consumptionQueryFilter: ConsumptionQueryFilter) {
        viewModelScope.launch(dispatcher) {
            // We force the operation to fail if the first tariff start date is not known
            val firstTariffStartDate = _uiState.value.userProfile?.getSelectedElectricityMeterPoint()?.getFirstTariffStartDate() ?: Instant.DISTANT_FUTURE
            val newConsumptionQueryFilter = consumptionQueryFilter.navigateBackward(firstTariffStartDate = firstTariffStartDate)
            if (newConsumptionQueryFilter == null) {
                Logger.e("UsageViewModel", message = { "navigateBackward request declined." })
                _uiState.update { currentUiState ->
                    currentUiState.filterErrorAndStopLoading(throwable = IllegalArgumentException(getString(resource = Res.string.usage_error_date_out_of_range)))
                }
            } else {
                refresh(consumptionQueryFilter = newConsumptionQueryFilter)
            }
        }
    }

    fun onNextTimeFrame(consumptionQueryFilter: ConsumptionQueryFilter) {
        viewModelScope.launch(dispatcher) {
            val newConsumptionQueryFilter = consumptionQueryFilter.navigateForward()
            if (newConsumptionQueryFilter == null) {
                Logger.e("UsageViewModel", message = { "navigateForward request declined." })
                _uiState.update { currentUiState ->
                    currentUiState.filterErrorAndStopLoading(throwable = IllegalArgumentException(getString(resource = Res.string.usage_error_date_out_of_range)))
                }
            } else {
                refresh(consumptionQueryFilter = newConsumptionQueryFilter)
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

    private suspend fun propagateInsightConsumptionsAndStopLoading(
        consumptionQueryFilter: ConsumptionQueryFilter,
        consumptionWithCost: List<ConsumptionWithCost>,
        tariff: Tariff?,
    ) {
        val consumptions = consumptionWithCost.map { it.consumption }
        val consumptionRange = consumptions.getConsumptionRange()
        val consumptionGroupedCells = consumptionQueryFilter.groupChartCells(consumptions = consumptions)
        val insights = generateUsageInsightsUseCase(
            tariff = tariff,
            consumptionWithCost = consumptionWithCost,
        )

        _uiState.update { currentUiState ->
            currentUiState.copy(
                consumptionQueryFilter = consumptionQueryFilter,
                consumptionGroupedCells = consumptionGroupedCells,
                consumptionRange = consumptionRange,
                barChartData = BarChartData.fromConsumptions(
                    consumptions = consumptions,
                    presentationStyle = consumptionQueryFilter.presentationStyle,
                ),
                requestedScreenType = UsageScreenType.Chart,
                insights = insights,
                isLoading = false,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("UsageViewModel", message = { "onCleared" })
    }
}
