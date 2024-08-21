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
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.usecase.account.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetLatestProductByKeywordUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetTariffRatesUseCase
import com.rwmobi.kunigami.ui.destinations.agile.AgileScreenType
import com.rwmobi.kunigami.ui.destinations.agile.AgileUIState
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.product.RetailRegion
import com.rwmobi.kunigami.ui.model.rate.RateGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min
import kotlin.time.Duration

class AgileViewModel(
    private val getLatestProductByKeywordUseCase: GetLatestProductByKeywordUseCase,
    private val getTariffRatesUseCase: GetTariffRatesUseCase,
    private val getStandardUnitRateUseCase: GetStandardUnitRateUseCase,
    private val syncUserProfileUseCase: SyncUserProfileUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AgileUIState> = MutableStateFlow(AgileUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val fallBackAgileProductCode = "AGILE-24-04-03"
    private val fallBackFixedProductCode = "OE-FIX-12M-24-06-28"
    private val fallBackFlexibleProductCode = "VAR-22-11-01"
    private val demoRetailRegion = RetailRegion.EASTERN_ENGLAND

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
                val currentAgreement = currentUserProfile?.getSelectedElectricityMeterPoint()?.lookupAgreement(referencePoint = Clock.System.now())
                val region = currentAgreement?.tariffCode?.let { Tariff.getRetailRegion(tariffCode = it) } ?: demoRetailRegion
                val productCode = getAgileProductCode(currentTariffCode = currentAgreement?.tariffCode)

                fetchReferenceTariffs(region = region)

                getAgileRates(
                    productCode = productCode,
                    region = region,
                )
                getAgileTariffAndStopLoading(
                    productCode = productCode,
                    region = region,
                )
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
                    Logger.e("Unable to retrieve your account details", throwable = throwable, tag = "AccountViewModel")
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable = throwable)
                    }
                }
                return null
            },
        )
    }

    private suspend fun getAgileProductCode(currentTariffCode: String?): String {
        if (currentTariffCode != null && Tariff.isAgileProduct(tariffCode = currentTariffCode)) {
            val currentProductCode = Tariff.extractProductCode(tariffCode = currentTariffCode)
            currentProductCode?.let { return it }
        }

        return getLatestAgileProductCode()
    }

    private suspend fun getAgileRates(
        productCode: String,
        region: RetailRegion,
    ) {
        val currentTime = Clock.System.now().atStartOfHour()
        val periodTo = currentTime.plus(duration = Duration.parse("1d"))

        getStandardUnitRateUseCase(
            tariffCode = "E-1R-$productCode-${region.code}",
            period = currentTime..periodTo,
        ).fold(
            onSuccess = { rates ->
                val rateRange = if (rates.isEmpty()) {
                    0.0..calculateMaxChartRange(vatIncludedUnitRate = 0.0) // Return a default range if the list is empty
                } else {
                    min(0.0, floor(rates.minOf { it.vatInclusivePrice } * 10) / 10.0)..calculateMaxChartRange(vatIncludedUnitRate = rates.maxOf { it.vatInclusivePrice })
                }

                val rateGroupedCells = groupChartCells(rates = rates)
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        requestedScreenType = AgileScreenType.Chart,
                        rateGroupedCells = rateGroupedCells,
                        rateRange = rateRange,
                        barChartData = BarChartData.fromRates(rates = rates),
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

    /***
     * Return the max of flexible unit rate (if available), fixed unit rate (if available) and the given max rate supplied by the caller.
     */
    private fun calculateMaxChartRange(vatIncludedUnitRate: Double): Double {
        return ceil(
            maxOf(
                _uiState.value.latestFixedTariff?.vatInclusiveStandardUnitRate ?: 0.0,
                _uiState.value.latestFlexibleTariff?.vatInclusiveStandardUnitRate ?: 0.0,
                vatIncludedUnitRate,
            ) * 10,
        ) / 10.0
    }

    private suspend fun getAgileTariffAndStopLoading(
        productCode: String,
        region: RetailRegion,
    ) {
        getTariffRatesUseCase(
            tariffCode = "E-1R-$productCode-${region.code}",
        ).fold(
            onSuccess = { agileTariff ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        agileTariff = agileTariff,
                        isLoading = false,
                    )
                }
            },
            onFailure = { throwable ->
                Logger.e("AgileViewModel", throwable = throwable, message = { "Error when retrieving Agile tariff details" })
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        agileTariff = null,
                        isLoading = false,
                    )
                }
            },
        )
    }

    private fun groupChartCells(rates: List<Rate>): List<RateGroup> {
        return rates
            .groupBy { it.validity.start.getLocalDateString() }
            .map { (date, items) -> RateGroup(title = date, rates = items) }
    }

    private suspend fun getLatestAgileProductCode(): String {
        return getLatestProductByKeywordUseCase(keyword = "AGILE") ?: fallBackAgileProductCode
    }

    /**
     * Best effort to fetch reference tariffs for comparison. No harm if it fails, so it won't stop loading.
     */
    private suspend fun fetchReferenceTariffs(region: RetailRegion) {
        val fixedProductCode = getLatestProductByKeywordUseCase(keyword = "OE-FIX-12M") ?: fallBackFixedProductCode
        getTariffRatesUseCase(
            tariffCode = "E-1R-$fixedProductCode-${region.code}",
        ).fold(
            onSuccess = { fixedTariff ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        latestFixedTariff = fixedTariff,
                    )
                }
            },
            onFailure = { throwable ->
                Logger.e("AgileViewModel", throwable = throwable, message = { "Error when retrieving fixed tariff details" })
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        latestFixedTariff = null,
                    )
                }
            },
        )

        val flexibleProductCode = getLatestProductByKeywordUseCase(keyword = "VAR-") ?: fallBackFlexibleProductCode
        getTariffRatesUseCase(
            tariffCode = "E-1R-$flexibleProductCode-${region.code}",
        ).fold(
            onSuccess = { flexibleTariff ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        latestFlexibleTariff = flexibleTariff,
                    )
                }
            },
            onFailure = { throwable ->
                Logger.e("AgileViewModel", throwable = throwable, message = { "Error when retrieving flexible tariff details" })
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        latestFlexibleTariff = null,
                    )
                }
            },
        )
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("AgileViewModel", message = { "onCleared" })
    }
}
