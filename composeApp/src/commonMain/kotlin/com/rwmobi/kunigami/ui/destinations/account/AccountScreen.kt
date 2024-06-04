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
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.ui.components.ErrorScreenHandler
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.composehelper.conditionalBlur
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.previewsampledata.FakeDemoUserProfile
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension

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
        if (uiState.requestedScreenType is AccountScreenType.ErrorScreen) {
            ErrorScreenHandler(
                modifier = Modifier.fillMaxSize(),
                specialErrorScreen = uiState.requestedScreenType.specialErrorScreen,
                onRefresh = {
                    uiEvent.onRefresh()
                },
                onClearCredential = {
                    uiEvent.onSpecialErrorScreenShown()
                    uiEvent.onClearCredentialButtonClicked()
                },
            )
        } else {
            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxWidth(),
                lazyListState = lazyListState,
            ) { contentModifier ->
                LazyColumn(
                    modifier = contentModifier
                        .fillMaxWidth()
                        .conditionalBlur(enabled = uiState.isLoading),
                    state = lazyListState,
                ) {
                    when {
                        uiState.requestedScreenType is AccountScreenType.Onboarding -> {
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

                        uiState.requestedScreenType is AccountScreenType.Account && uiState.userProfile?.account != null -> {
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

                        else -> {
                            item(key = "noData") {
                                ErrorScreenHandler(
                                    modifier = Modifier.fillMaxSize(),
                                    specialErrorScreen = SpecialErrorScreen.NoData,
                                    onRefresh = {
                                        uiEvent.onRefresh()
                                    },
                                    onClearCredential = {
                                        uiEvent.onSpecialErrorScreenShown()
                                        uiEvent.onClearCredentialButtonClicked()
                                    },
                                )
                            }
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

    LaunchedEffect(uiState.requestedScreenType) {
        lazyListState.scrollToItem(index = 0)
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
                requestedScreenType = AccountScreenType.Account,
                userProfile = FakeDemoUserProfile.flexibleOctopusRegionADirectDebit,
                errorMessages = listOf(),
            ),
            uiEvent = AccountUIEvent(
                onClearCredentialButtonClicked = {},
                onSubmitCredentials = { _, _ -> },
                onMeterSerialNumberSelected = { _, _ -> },
                onRefresh = {},
                onErrorShown = {},
                onScrolledToTop = {},
                onShowSnackbar = {},
                onSpecialErrorScreenShown = {},
            ),
        )
    }
}
