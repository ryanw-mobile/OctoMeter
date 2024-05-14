/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    val scrollState = rememberScrollState()

    if (!uiState.isLoading) {
        if (uiState.account != null) {
            Column(
                modifier = modifier
                    .verticalScroll(state = scrollState)
                    .padding(horizontal = dimension.grid_2),
            ) {
                with(uiState.account) {
                    Text(text = "Account number: $accountNumber")
                    Text("$fullAddress")
                    movedInAt?.let {
                        Text("Moved in at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
                    }
                    movedOutAt?.let {
                        Text("Moved out at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
                    }

                    Spacer(modifier = Modifier.size(size = dimension.grid_1))

                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        text = "Meter information",
                    )

                    electricityMeterPoints.forEach { meterPoint ->
                        Text("MPAN: ${meterPoint.mpan}")
                        Text("Meter serial numbers:")
                        meterPoint.meterSerialNumbers.forEach {
                            Text(text = it)
                        }

                        Spacer(modifier = Modifier.size(size = dimension.grid_1))

                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            text = "Tariff",
                        )
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = meterPoint.currentAgreement.tariffCode,
                        )
                        Text(
                            text = "From ${meterPoint.currentAgreement.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).date} to ${meterPoint.currentAgreement.validTo?.toLocalDateTime(TimeZone.currentSystemDefault())?.date}",
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = uiState.tariff?.fullName ?: "",
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = uiState.tariff?.displayName ?: "",
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = uiState.tariff?.code ?: "",
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "Unit Rate: ${uiState.tariff?.vatInclusiveUnitRate ?: ""}",
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "Standing charge: £${uiState.tariff?.vatInclusiveStandingCharge ?: ""}",
                        )
                    }
                }

                Spacer(modifier = Modifier.size(size = dimension.grid_3))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                ) {
                    Text("Log Out")
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = dimension.grid_1)
                        .fillMaxWidth(),
                )

                Text(
                    modifier = modifier.padding(vertical = dimension.grid_2),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    text = "Version 1.0.0\nAPI provided by Octopus Energy subject to their terms of service.",
                )
            }
        } else {
            Column(
                modifier = modifier.verticalScroll(state = scrollState),
            ) {
                Text(text = "Account details not found or you don't have an account yet.")
            }
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
