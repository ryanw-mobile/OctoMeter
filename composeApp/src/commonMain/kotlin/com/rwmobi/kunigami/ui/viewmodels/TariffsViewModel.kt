/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import com.rwmobi.kunigami.domain.usecase.GetFilteredProductsUseCase
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffScreenLayout
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffsScreenType
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffsUIState
import com.rwmobi.kunigami.ui.extensions.generateRandomLong
import com.rwmobi.kunigami.ui.extensions.getPlatformType
import com.rwmobi.kunigami.ui.model.ErrorMessage
import com.rwmobi.kunigami.ui.model.PlatformType
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_load_account
import org.jetbrains.compose.resources.getString

class TariffsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val getFilteredProductsUseCase: GetFilteredProductsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _uiState: MutableStateFlow<TariffsUIState> = MutableStateFlow(TariffsUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()
    val windowWidthCompact: Dp = 599.dp

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun notifyScreenSizeChanged(screenSizeInfo: ScreenSizeInfo, windowSizeClass: WindowSizeClass) {
        _uiState.update { currentUiState ->
            val platform = windowSizeClass.getPlatformType()
            val requestedLayout = when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> TariffScreenLayout.Compact(useBottomSheet = platform != PlatformType.DESKTOP)
                WindowWidthSizeClass.Medium -> TariffScreenLayout.Wide(useBottomSheet = platform != PlatformType.DESKTOP)
                else -> TariffScreenLayout.ListDetailPane
            }
            val requestedWideListLayout = when {
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> false
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> true
                (screenSizeInfo.widthDp / 2) > windowWidthCompact -> true // List pane width
                else -> false
            }

            currentUiState.copy(
                requestedLayout = requestedLayout,
                requestedWideListLayout = requestedWideListLayout,
            )
        }
        calculateScreenType()
    }

    fun refresh() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            val filteredProducts = getFilteredProductsUseCase()
            filteredProducts.fold(
                onSuccess = { products ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            productSummaries = products,
                        )
                    }
                    calculateScreenType()
                },
                onFailure = { throwable ->
                    Logger.e("TariffsViewModel", throwable = throwable, message = { "Error when retrieving tariffs" })
                    filterError(throwable = throwable)
                },
            )
            stopLoading()
        }
    }

    fun getProductDetails(productCode: String) {
        startLoading()
        viewModelScope.launch(dispatcher) {
            val getProductDetailsResult = restApiRepository.getProductDetails(
                productCode = productCode,
            )

            getProductDetailsResult.fold(
                onSuccess = { product ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            productDetails = product,
                        )
                    }
                    calculateScreenType()
                },
                onFailure = { throwable ->
                    Logger.e("TariffsViewModel", throwable = throwable, message = { "Error when retrieving product details" })
                    filterError(throwable = throwable)
                },
            )
            stopLoading()
        }
    }

    fun onProductDetailsDismissed() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                productDetails = null,
            )
        }
        calculateScreenType()
    }

    fun onSpecialErrorScreenShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestedScreenType = null,
            )
        }
    }

    fun clearCredentials() {
        viewModelScope.launch {
            userPreferencesRepository.clearCredentials()
            refresh()
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
                requestedScreenType = null,
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

    private fun calculateScreenType() {
        _uiState.update { currentUiState ->
            val requestedScreenType = if (currentUiState.requestedScreenType is TariffsScreenType.ErrorScreen) {
                currentUiState.requestedScreenType
            } else if (currentUiState.productDetails == null ||
                currentUiState.requestedLayout is TariffScreenLayout.ListDetailPane ||
                currentUiState.requestedLayout == TariffScreenLayout.Compact(useBottomSheet = true) ||
                currentUiState.requestedLayout == TariffScreenLayout.Wide(useBottomSheet = true)
            ) {
                TariffsScreenType.TariffsList
            } else {
                // currentUiState.productDetails != null
                TariffsScreenType.FullScreenTariffsDetail
            }

            currentUiState.copy(
                requestedScreenType = requestedScreenType,
            )
        }
    }

    private suspend fun filterError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        requestedScreenType = TariffsScreenType.ErrorScreen(specialErrorScreen = SpecialErrorScreen.HttpError(statusCode = throwable.httpStatusCode)),
                    )
                }
            }

            is UnresolvedAddressException -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        requestedScreenType = TariffsScreenType.ErrorScreen(specialErrorScreen = SpecialErrorScreen.NetworkError),
                    )
                }
            }

            else -> {
                updateUIForError(message = throwable.message ?: getString(resource = Res.string.account_error_load_account))
            }
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
        Logger.v("TariffsViewModel", message = { "onCleared" })
    }
}
