/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun UsageScreen(
    modifier: Modifier = Modifier,
    uiState: UsageUIState,
    uiEvent: UsageUIEvent,
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
    val scrollState = rememberScrollState()

    if (!uiState.isLoading) {
        Column(
            modifier = modifier
                .verticalScroll(state = scrollState)
                .padding(horizontal = dimension.grid_2),
        ) {
            uiState.consumptions.forEach { consumption ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val timeLabel = consumption.intervalStart.toLocalDateTime(TimeZone.currentSystemDefault())
                    Text(
                        modifier = Modifier.weight(1.0f),
                        text = "${timeLabel.date} ${timeLabel.time}",
                    )

                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        fontWeight = FontWeight.Bold,
                        text = "${consumption.consumption}",
                    )
                }
            }
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }
}
