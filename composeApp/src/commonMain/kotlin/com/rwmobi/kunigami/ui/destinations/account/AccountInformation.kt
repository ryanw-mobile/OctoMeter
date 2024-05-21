/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.extensions.formatDate
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Agreement
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.components.DefaultFailureRetryScreen
import com.rwmobi.kunigami.ui.destinations.account.components.AppInfoFooter
import com.rwmobi.kunigami.ui.destinations.account.components.ClearCredentialSectionCompact
import com.rwmobi.kunigami.ui.destinations.account.components.ClearCredentialSectionWide
import com.rwmobi.kunigami.ui.destinations.account.components.ElectricityMeterPointCard
import com.rwmobi.kunigami.ui.destinations.account.components.UpdateAPIKeyCard
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.account_error_account_empty
import kunigami.composeapp.generated.resources.account_moved_in
import kunigami.composeapp.generated.resources.account_moved_out
import kunigami.composeapp.generated.resources.account_number
import kunigami.composeapp.generated.resources.account_unknown_installation_address
import kunigami.composeapp.generated.resources.bulb
import kunigami.composeapp.generated.resources.retry
import kunigami.composeapp.generated.resources.unlink
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun AccountInformationScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUIState,
    uiEvent: AccountUIEvent,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
    ) {
        if (uiState.account == null) {
            DefaultFailureRetryScreen(
                modifier = modifier.fillMaxSize(),
                text = stringResource(resource = Res.string.account_error_account_empty),
                icon = painterResource(resource = Res.drawable.unlink),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = uiEvent.onRefresh,
                secondaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onSecondaryButtonClicked = uiEvent.onClearCredentialButtonClicked,
            )
        } else {
            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(end = dimension.grid_2),
                    tint = MaterialTheme.colorScheme.secondary,
                    painter = painterResource(resource = Res.drawable.bulb),
                    contentDescription = null,
                )

                Text(
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(resource = Res.string.account_number, uiState.account.accountNumber),
                )
            }

            uiState.account.fullAddress?.let {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = it,
                )
            } ?: run {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = stringResource(resource = Res.string.account_unknown_installation_address),
                )
            }

            uiState.account.movedInAt?.let {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(resource = Res.string.account_moved_in, it.formatDate()),
                )
            }

            uiState.account.movedOutAt?.let {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(resource = Res.string.account_moved_out, it.formatDate()),
                )
            }

            uiState.account.electricityMeterPoints.forEach { meterPoint ->
                ElectricityMeterPointCard(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMpan = uiState.selectedMpan,
                    selectedMeterSerialNumber = uiState.selectedMeterSerialNumber,
                    meterPoint = meterPoint,
                    tariff = uiState.tariff,
                    requestedLayout = uiState.requestedLayout,
                    onMeterSerialNumberSelected = uiEvent.onMeterSerialNumberSelected,
                    onReloadTariff = uiEvent.onRefresh,
                )
            }
        }

        UpdateAPIKeyCard(
            modifier = Modifier.fillMaxWidth(),
            onUpdateAPIKeyClicked = uiEvent.onUpdateApiKeyClicked,
        )

        BoxWithConstraints {
            if (uiState.requestedLayout == AccountScreenLayout.Compact) {
                ClearCredentialSectionCompact(
                    modifier = Modifier.fillMaxWidth(),
                    onClearCredentialButtonClicked = uiEvent.onClearCredentialButtonClicked,
                )
            } else {
                ClearCredentialSectionWide(
                    modifier = Modifier.fillMaxWidth(),
                    onClearCredentialButtonClicked = uiEvent.onClearCredentialButtonClicked,
                )
            }
        }

        AppInfoFooter(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            AccountInformationScreen(
                modifier = Modifier.padding(all = 32.dp),
                uiState = AccountUIState(
                    isLoading = false,
                    isDemoMode = false,
                    requestedLayout = AccountScreenLayout.WideWrapped,
                    selectedMpan = "1200000345678",
                    selectedMeterSerialNumber = "11A1234567",
                    account = // null,
                    Account(
                        id = 8638,
                        accountNumber = "A-1234A1B1",
                        fullAddress = "Address line 1\nAddress line 2\nAddress line 3\nAddress line 4",
                        movedInAt = Clock.System.now(),
                        movedOutAt = null,
                        electricityMeterPoints = listOf(
                            ElectricityMeterPoint(
                                mpan = "1200000345678",
                                meterSerialNumbers = listOf("11A1234567", "11A12345A7"),
                                currentAgreement = Agreement(
                                    tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                                    validFrom = Clock.System.now(),
                                    validTo = Clock.System.now().plus(Duration.parse("365d")),
                                ),
                            ),
                            ElectricityMeterPoint(
                                mpan = "1200000345670",
                                meterSerialNumbers = listOf("11A1234560"),
                                currentAgreement = Agreement(
                                    tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                                    validFrom = Clock.System.now(),
                                    validTo = Clock.System.now().plus(Duration.parse("365d")),
                                ),
                            ),
                        ),
                    ),
                    tariff = Tariff(
                        code = "E-1R-AGILE-FLEX-22-11-25-A",
                        fullName = "Octopus 12M Fixed April 2024 v1",
                        displayName = "Octopus 12M Fixed",
                        vatInclusiveUnitRate = 99.257,
                        vatInclusiveStandingCharge = 94.682,
                    ),
                    errorMessages = listOf(),
                ),
                uiEvent = AccountUIEvent(
                    onClearCredentialButtonClicked = {},
                    onUpdateApiKeyClicked = {},
                    onSubmitCredentials = { _, _ -> },
                    onRefresh = {},
                    onMeterSerialNumberSelected = { _, _ -> },
                    onErrorShown = {},
                    onScrolledToTop = {},
                    onShowSnackbar = {},
                ),
            )
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    AppTheme {
        Surface {
            AccountInformationScreen(
                modifier = Modifier.padding(all = 32.dp),
                uiState = AccountUIState(
                    isLoading = false,
                    isDemoMode = false,
                    requestedLayout = AccountScreenLayout.WideWrapped,
                    selectedMpan = "1200000345678",
                    selectedMeterSerialNumber = "11A1234567",
                    account = null,
                    tariff = Tariff(
                        code = "E-1R-AGILE-FLEX-22-11-25-A",
                        fullName = "Octopus 12M Fixed April 2024 v1",
                        displayName = "Octopus 12M Fixed",
                        vatInclusiveUnitRate = 99.257,
                        vatInclusiveStandingCharge = 94.682,
                    ),
                    errorMessages = listOf(),
                ),
                uiEvent = AccountUIEvent(
                    onClearCredentialButtonClicked = {},
                    onUpdateApiKeyClicked = {},
                    onSubmitCredentials = { _, _ -> },
                    onRefresh = {},
                    onMeterSerialNumberSelected = { _, _ -> },
                    onErrorShown = {},
                    onScrolledToTop = {},
                    onShowSnackbar = {},
                ),
            )
        }
    }
}
