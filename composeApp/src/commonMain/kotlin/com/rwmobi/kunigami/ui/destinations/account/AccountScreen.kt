/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.ui.theme.AppTheme

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

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(state = scrollState),
    ) {
        Text(text = "Account screen")
        Text("${uiState.account?.fullAddress}")
        Text("Moved in at: ${uiState.account?.movedInAt}")
        uiState.account?.movedOutAt?.let {
            Text("Moved out at: $it")
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
                account = null,
                isLoading = false,
                errorMessages = listOf(),
            ),
            uiEvent = AccountUIEvent(
                onRefresh = {},
                onErrorShown = {},
                onShowSnackbar = {},
            ),
        )
    }
}
