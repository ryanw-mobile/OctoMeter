/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock
import kotlin.time.Duration

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUIState,
    uiEvent: AccountUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val dimension = LocalDensity.current.getDimension()
    val lazyListState = rememberLazyListState()

    Box(modifier = modifier) {
        ScrollbarMultiplatform(
            modifier = Modifier.fillMaxWidth(),
            lazyListState = lazyListState,
        ) { contentModifier ->

            LazyColumn(
                modifier = contentModifier.fillMaxWidth(),
                state = lazyListState,
            ) {
                if (uiState.isDemoMode == true) {
                    item(key = "onboarding") {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            val widthConstraintModifier = when (uiState.requestedLayout) {
                                is AccountScreenLayout.Compact -> Modifier.fillMaxWidth()
                                is AccountScreenLayout.Wide -> Modifier.fillMaxWidth()
                                else -> Modifier.widthIn(max = dimension.windowWidthMedium)
                            }

                            OnboardingScreen(
                                modifier = widthConstraintModifier.padding(all = dimension.grid_2),
                                uiState = uiState,
                                uiEvent = uiEvent,
                            )
                        }
                    }
                }

                if (uiState.isDemoMode == false && uiState.userProfile?.account != null) {
                    item(key = "account") {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            val widthConstraintModifier = when (uiState.requestedLayout) {
                                is AccountScreenLayout.Compact -> Modifier.fillMaxWidth()
                                is AccountScreenLayout.Wide -> Modifier.fillMaxWidth()
                                else -> Modifier.widthIn(max = dimension.windowWidthMedium)
                            }

                            AccountInformationScreen(
                                modifier = widthConstraintModifier.padding(horizontal = dimension.grid_2),
                                uiState = uiState,
                                uiEvent = uiEvent,
                            )
                        }
                    }
                }
            }
        }

        if (uiState.isLoading) {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }

    LaunchedEffect(uiState.requestScrollToTop) {
        if (uiState.requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            uiEvent.onScrolledToTop()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        AccountScreen(
            uiState = AccountUIState(
                isLoading = false,
                isDemoMode = false,
                userProfile = UserProfile(
                    account = Account(
                        id = 8638,
                        accountNumber = "A-1234A1B1",
                        fullAddress = "Address line 1\nAddress line 2\nAddress line 3\nAddress line 4",
                        movedInAt = Clock.System.now(),
                        movedOutAt = null,
                        electricityMeterPoints = listOf(
                            ElectricityMeterPoint(
                                mpan = "1200000345678",
                                meterSerialNumbers = listOf("11A1234567"),
                                currentAgreement = Agreement(
                                    tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                                    validFrom = Clock.System.now(),
                                    validTo = Clock.System.now().plus(Duration.parse("365d")),
                                ),
                            ),
                        ),
                    ),
                    tariff = Tariff(
                        productCode = "AGILE-FLEX-22-11-25",
                        tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                        fullName = "Octopus 12M Fixed April 2024 v1",
                        displayName = "Octopus 12M Fixed",
                        vatInclusiveUnitRate = 99.257,
                        vatInclusiveStandingCharge = 94.682,
                        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                        availableFrom = Clock.System.now(),
                        availableTo = null,
                    ),
                ),
                errorMessages = listOf(),
            ),
            uiEvent = AccountUIEvent(
                onClearCredentialButtonClicked = {},
                onUpdateApiKeyClicked = {},
                onSubmitCredentials = { _, _ -> },
                onMeterSerialNumberSelected = { _, _ -> },
                onRefresh = {},
                onErrorShown = {},
                onScrolledToTop = {},
                onShowSnackbar = {},
            ),
        )
    }
}
