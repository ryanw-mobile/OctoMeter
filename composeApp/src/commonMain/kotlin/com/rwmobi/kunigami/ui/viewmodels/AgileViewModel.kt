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
import com.rwmobi.kunigami.domain.extensions.atStartOfHour
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.usecase.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffRatesUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.ui.destinations.agile.AgileScreenType
import com.rwmobi.kunigami.ui.destinations.agile.AgileUIState
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.util.toString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
import org.jetbrains.compose.resources.getString
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min
import kotlin.time.Duration

class AgileViewModel(
    private val getTariffRatesUseCase: GetTariffRatesUseCase,
    private val getStandardUnitRateUseCase: GetStandardUnitRateUseCase,
    private val syncUserProfileUseCase: SyncUserProfileUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AgileUIState> = MutableStateFlow(AgileUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    // TODO: Low priority - not my business. Get Agile product code from user preferences.
    private val agileProductCode = "AGILE-24-04-03"
    private val demoRetailRegion = "A"

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun refresh() {
        _uiState.update { currentUiState ->
            currentUiState.copy(isLoading = true)
        }

        viewModelScope.launch(dispatcher) {
            val currentUserProfile = getUserProfile()
            if (currentUserProfile != null || _uiState.value.isDemoMode == true) {
                val region = currentUserProfile?.tariffSummary?.getRetailRegion() ?: demoRetailRegion
                getAgileRates(region = region)
                getAgileTariffAndStopLoading(region = region)
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
            currentUiState.copy(requestScrollToTop = enabled)
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
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable)
                    }
                }
                return null
            },
        )
    }

    private suspend fun getAgileRates(
        region: String,
    ) {
        val currentTime = Clock.System.now().atStartOfHour()
        val periodTo = currentTime.plus(duration = Duration.parse("1d"))

        getStandardUnitRateUseCase(
            productCode = agileProductCode,
            tariffCode = "E-1R-$agileProductCode-$region",
            periodFrom = currentTime,
            periodTo = periodTo,
        ).fold(
            onSuccess = { rates ->
                val rateRange = if (rates.isEmpty()) {
                    0.0..0.0 // Return a default range if the list is empty
                } else {
                    min(0.0, floor(rates.minOf { it.vatInclusivePrice } * 10) / 10.0)..ceil(rates.maxOf { it.vatInclusivePrice } * 10) / 10.0
                }

                val verticalBarPlotEntries: List<VerticalBarPlotEntry<Int, Double>> = buildList {
                    rates.forEachIndexed { index, rate ->
                        add(
                            element = DefaultVerticalBarPlotEntry(
                                x = index,
                                y = if (rate.vatInclusivePrice >= 0) {
                                    DefaultVerticalBarPosition(yMin = 0.0, yMax = rate.vatInclusivePrice)
                                } else {
                                    DefaultVerticalBarPosition(yMin = rate.vatInclusivePrice, yMax = 0.0)
                                },
                            ),
                        )
                    }
                }

                val labels = generateChartLabels(rates = rates)
                val rateGroupedCells = groupChartCells(rates = rates)
                val toolTips = generateChartToolTips(rates = rates)

                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        requestedScreenType = AgileScreenType.Chart,
                        rateGroupedCells = rateGroupedCells,
                        rateRange = rateRange,
                        barChartData = BarChartData(
                            verticalBarPlotEntries = verticalBarPlotEntries,
                            labels = labels,
                            tooltips = toolTips,
                        ),
                    )
                }
            },
            onFailure = { throwable ->
                Logger.e("AgileViewModel", throwable = throwable, message = { "Error when retrieving rates" })
                _uiState.update { currentUiState ->
                    currentUiState.filterErrorAndStopLoading(throwable = throwable)
                }
            },
        )
    }

    private suspend fun getAgileTariffAndStopLoading(
        region: String,
    ) {
        getTariffRatesUseCase(
            productCode = agileProductCode,
            tariffCode = "E-1R-$agileProductCode-$region",
        ).fold(
            onSuccess = { agileTariff ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        agileTariffSummary = agileTariff,
                        isLoading = false,
                    )
                }
            },
            onFailure = { throwable ->
                Logger.e("AgileViewModel", throwable = throwable, message = { "Error when retrieving Agile tariff details" })
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        agileTariffSummary = null,
                        isLoading = false,
                    )
                }
            },
        )
    }

    private fun generateChartLabels(rates: List<Rate>): Map<Int, String> {
        return buildMap {
            var lastRateValue: Int? = null
            rates.forEachIndexed { index, rate ->
                val currentTime = rate.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                if (currentTime != lastRateValue) {
                    put(key = index, value = currentTime.toString().padStart(2, '0'))
                    lastRateValue = currentTime
                }
            }
        }
    }

    private fun groupChartCells(rates: List<Rate>): List<RateGroupedCells> {
        return rates
            .groupBy { it.validFrom.getLocalDateString() }
            .map { (date, items) -> RateGroupedCells(title = date, rates = items) }
    }

    private fun generateChartToolTips(rates: List<Rate>): List<String> {
        return rates.map { rate ->
            val timeRange = rate.validFrom.getLocalHHMMString() +
                (rate.validTo?.let { "- ${it.getLocalHHMMString()}" } ?: "")
            "$timeRange\n${rate.vatInclusivePrice.toString(precision = 2)}p"
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("AgileViewModel", message = { "onCleared" })
    }
}
