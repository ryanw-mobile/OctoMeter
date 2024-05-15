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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.destinations.account.components.AccountInformation
import com.rwmobi.kunigami.ui.destinations.account.components.AppInfoFooter
import com.rwmobi.kunigami.ui.destinations.account.components.ClearCredentialSection
import com.rwmobi.kunigami.ui.destinations.account.components.Onboarding
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock

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
                if (!uiState.isLoading && uiState.isDemoMode) {
                    item(key = "onboarding") {
                        Onboarding(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = dimension.grid_4),
                        )
                    }
                }

                if (!uiState.isDemoMode && uiState.account != null) {
                    item(key = "account") {
                        AccountInformation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimension.grid_4),
                            account = uiState.account,
                            tariff = uiState.tariff,
                        )
                    }
                }

                if (!uiState.isDemoMode && uiState.account != null) {
                    item(key = "toDemoMode") {
                        ClearCredentialSection(
                            modifier = modifier.fillMaxWidth(),
                            onClearCredentialButtonClicked = uiEvent.onClearCredentialButtonClicked,
                        )
                    }
                }

                if (!uiState.isLoading) {
                    item(key = "footer") { AppInfoFooter(modifier = Modifier.fillMaxWidth()) }
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
}

@Composable
@Preview
private fun AccountScreenPreview() {
    AppTheme {
        AccountScreen(
            uiState = AccountUIState(
                account = Account(
                    id = 8638,
                    accountNumber = "Marquitta",
                    fullAddress = "Address line 1\nAddress line 2\nAddress line 3\nAddress line 4",
                    movedInAt = Clock.System.now(),
                    movedOutAt = null,
                    electricityMeterPoints = listOf(),
                ),
                isLoading = false,
                isDemoMode = false,
                errorMessages = listOf(),
            ),
            uiEvent = AccountUIEvent(
                onClearCredentialButtonClicked = {},
                onRefresh = {},
                onErrorShown = {},
                onShowSnackbar = {},
            ),
        )
    }
}
