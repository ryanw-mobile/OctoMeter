/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.ui.components.DualTitleBar
import com.rwmobi.kunigami.ui.components.ErrorScreenHandler
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.composehelper.conditionalBlur
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.previewsampledata.FakeDemoUserProfile
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.navigation_account
import org.jetbrains.compose.resources.stringResource

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

    val lazyListState = rememberLazyListState()

    Box(modifier = modifier) {
        if (uiState.requestedScreenType is AccountScreenType.Error) {
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
                Column(
                    modifier = contentModifier.fillMaxSize(),
                ) {
                    val accountNumber = uiState.userProfile?.account?.accountNumber
                    if (accountNumber != null) {
                        DualTitleBar(
                            modifier = Modifier
                                .background(color = AppTheme.colorScheme.secondary)
                                .fillMaxWidth()
                                .height(height = AppTheme.dimens.minListItemHeight),
                            title = stringResource(resource = Res.string.navigation_account),
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.conditionalBlur(enabled = uiState.isLoading),
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
                                            is AccountScreenLayoutStyle.Compact -> Modifier.fillMaxWidth()
                                            is AccountScreenLayoutStyle.Wide -> Modifier.fillMaxWidth()
                                            else -> Modifier.widthIn(max = AppTheme.dimens.windowWidthMedium)
                                        }

                                        OnboardingScreen(
                                            modifier = widthConstraintModifier.padding(all = AppTheme.dimens.grid_2),
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
                                            is AccountScreenLayoutStyle.Compact -> Modifier.fillMaxWidth()
                                            is AccountScreenLayoutStyle.Wide -> Modifier.fillMaxWidth()
                                            else -> Modifier.widthIn(max = AppTheme.dimens.windowWidthMedium)
                                        }

                                        AccountInformationScreen(
                                            modifier = widthConstraintModifier.padding(horizontal = AppTheme.dimens.grid_2),
                                            uiState = uiState,
                                            uiEvent = uiEvent,
                                        )
                                    }
                                }
                            }

                            !uiState.isLoading -> {
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
                onSubmitCredentials = { _, _, _ -> },
                onMeterSerialNumberSelected = { _, _ -> },
                onRefresh = {},
                onErrorShown = {},
                onScrolledToTop = {},
                onClearCache = {},
                onShowSnackbar = {},
                onSpecialErrorScreenShown = {},
            ),
        )
    }
}
