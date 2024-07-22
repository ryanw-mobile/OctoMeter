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
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.usecase.GetFilteredProductsUseCase
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffsUIState
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TariffsViewModel(
    private val octopusApiRepository: OctopusApiRepository,
    private val getFilteredProductsUseCase: GetFilteredProductsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TariffsUIState> = MutableStateFlow(TariffsUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun notifyScreenSizeChanged(screenSizeInfo: ScreenSizeInfo, windowSizeClass: WindowSizeClass) {
        _uiState.update { currentUiState ->
            currentUiState.updateLayoutType(
                screenSizeInfo = screenSizeInfo,
                windowSizeClass = windowSizeClass,
            ).updateScreenType()
        }
    }

    fun refresh() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            // TODO: Call use case and update UI State so we at least have a default postcode to start with.
            val postcode = (_uiState.value.queryPostCode) ?: "WC1X 0ND"
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    queryPostCode = postcode,
                )
            }

            getFilteredProducts()
        }
    }

    fun onQueryPostcode(postcode: String) {
        startLoading()
        viewModelScope.launch(dispatcher) {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    queryPostCode = postcode,
                )
            }

            getFilteredProducts()
        }
    }

    fun getProductDetails(productCode: String) {
        startLoading()
        viewModelScope.launch(dispatcher) {
            val getProductDetailsResult = octopusApiRepository.getProductDetails(productCode)
            getProductDetailsResult.fold(
                onSuccess = { product ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            productDetails = product,
                            isLoading = false,
                        ).updateScreenType()
                    }
                },
                onFailure = { throwable ->
                    Logger.e("TariffsViewModel", throwable = throwable, message = { "Error when retrieving product details" })
                    _uiState.update { currentUiState ->
                        currentUiState.filterErrorAndStopLoading(throwable)
                    }
                },
            )
        }
    }

    fun onProductDetailsDismissed() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                productDetails = null,
            ).updateScreenType()
        }
    }

    fun onSpecialErrorScreenShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestedScreenType = null,
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

    private suspend fun getFilteredProducts() {
        // TODO: Call use case and update UI State so we at least have a default postcode to start with.
        val postcode = (_uiState.value.queryPostCode) ?: "WC1X 0ND"

        val filteredProducts = getFilteredProductsUseCase(postcode = postcode)
        filteredProducts.fold(
            onSuccess = { products ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        queryPostCode = postcode,
                        requestedScreenType = null, // force recalculation
                        productSummaries = products,
                        productDetails = null,
                        isLoading = false,
                    ).updateScreenType()
                }
            },
            onFailure = { throwable ->
                Logger.e("TariffsViewModel", throwable = throwable, message = { "Error when retrieving tariffs" })
                _uiState.update { currentUiState ->
                    currentUiState.filterErrorAndStopLoading(throwable)
                }
            },
        )
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v("TariffsViewModel", message = { "onCleared" })
    }
}
