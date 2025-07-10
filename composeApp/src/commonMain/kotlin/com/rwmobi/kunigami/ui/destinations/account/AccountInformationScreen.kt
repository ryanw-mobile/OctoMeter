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

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.MessageActionScreen
import com.rwmobi.kunigami.ui.destinations.account.components.AccountOperationButtonBar
import com.rwmobi.kunigami.ui.destinations.account.components.AppInfoFooter
import com.rwmobi.kunigami.ui.destinations.account.components.ElectricityMeterPointCard
import com.rwmobi.kunigami.ui.destinations.account.components.UpdateApiKeyDialog
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.account_error_account_empty
import kunigami.composeapp.generated.resources.account_moved_in
import kunigami.composeapp.generated.resources.account_moved_out
import kunigami.composeapp.generated.resources.account_number
import kunigami.composeapp.generated.resources.account_unknown_installation_address
import kunigami.composeapp.generated.resources.retry
import kunigami.composeapp.generated.resources.unlink
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountInformationScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUIState,
    uiEvent: AccountUIEvent,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_3),
    ) {
        if (uiState.userProfile?.account == null) {
            MessageActionScreen(
                modifier = modifier.fillMaxSize(),
                text = stringResource(resource = Res.string.account_error_account_empty),
                icon = painterResource(resource = Res.drawable.unlink),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = uiEvent.onRefresh,
                secondaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onSecondaryButtonClicked = uiEvent.onClearCredentialButtonClicked,
            )
        } else {
            Spacer(modifier = Modifier.height(height = AppTheme.dimens.grid_1))

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.grid_2),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.grid_2),
                ) {
                    Column {
                        Text(
                            style = AppTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            text = "Welcome back ${uiState.userProfile.account.preferredName}!",
                        )

                        Text(
                            modifier = Modifier.alpha(0.5f),
                            style = AppTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            text = stringResource(resource = Res.string.account_number, uiState.userProfile.account.accountNumber),
                        )
                    }

                    uiState.userProfile.account.fullAddress?.let {
                        Text(
                            style = AppTheme.typography.bodyLarge,
                            text = it,
                        )
                    } ?: run {
                        Text(
                            style = AppTheme.typography.bodyLarge,
                            text = stringResource(resource = Res.string.account_unknown_installation_address),
                        )
                    }

                    Column {
                        uiState.userProfile.account.movedInAt?.let {
                            Text(
                                style = AppTheme.typography.bodyMedium,
                                text = stringResource(resource = Res.string.account_moved_in, it.getLocalDateString()),
                            )
                        }

                        uiState.userProfile.account.movedOutAt?.let {
                            Text(
                                style = AppTheme.typography.bodyMedium,
                                text = stringResource(resource = Res.string.account_moved_out, it.getLocalDateString()),
                            )
                        }
                    }
                }
            }

            uiState.userProfile.account.electricityMeterPoints.forEach { meterPoint ->
                ElectricityMeterPointCard(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMpan = uiState.userProfile.selectedMpan,
                    selectedMeterSerialNumber = uiState.userProfile.selectedMeterSerialNumber,
                    meterPoint = meterPoint,
                    requestedLayout = uiState.requestedLayout,
                    onMeterSerialNumberSelected = uiEvent.onMeterSerialNumberSelected,
                    onReloadTariff = uiEvent.onRefresh,
                )
            }
        }

        var isUpdateAPIKeyDialogOpened by rememberSaveable { mutableStateOf(false) }
        if (isUpdateAPIKeyDialogOpened && uiState.userProfile?.account?.accountNumber != null) {
            UpdateApiKeyDialog(
                initialValue = "",
                onDismiss = { isUpdateAPIKeyDialogOpened = false },
                onUpdateAPIKey = { newKey ->
                    uiEvent.onSubmitCredentials(newKey, uiState.userProfile.account.accountNumber, { getString(resource = it) })
                    isUpdateAPIKeyDialogOpened = false
                },
            )
        }

        AccountOperationButtonBar(
            onUpdateApiKey = { isUpdateAPIKeyDialogOpened = true },
            onClearCache = { uiEvent.onClearCache { getString(resource = it) } },
            onSwitchToDemoMode = { uiEvent.onClearCredentialButtonClicked() },
        )

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
                    requestedScreenType = AccountScreenType.Account,
                    requestedLayout = AccountScreenLayoutStyle.WideWrapped,
                    userProfile = UserProfile(
                        selectedMpan = "1200000345678",
                        selectedMeterSerialNumber = "11A1234567",
                        account = AccountSamples.accountTwoElectricityMeterPoint,
                    ),
                    errorMessages = listOf(),
                ),
                uiEvent = AccountUIEvent(
                    onClearCredentialButtonClicked = {},
                    onSubmitCredentials = { _, _, _ -> },
                    onRefresh = {},
                    onMeterSerialNumberSelected = { _, _ -> },
                    onErrorShown = {},
                    onClearCache = {},
                    onScrolledToTop = {},
                    onShowSnackbar = {},
                    onSpecialErrorScreenShown = {},
                ),
            )
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    CommonPreviewSetup {
        AccountInformationScreen(
            modifier = Modifier.padding(all = 32.dp),
            uiState = AccountUIState(
                isLoading = false,
                requestedScreenType = AccountScreenType.Error(specialErrorScreen = SpecialErrorScreen.NetworkError),
                requestedLayout = AccountScreenLayoutStyle.WideWrapped,
                userProfile = UserProfile(
                    selectedMpan = "1200000345678",
                    selectedMeterSerialNumber = "11A1234567",
                    account = AccountSamples.account928,
                ),
                errorMessages = listOf(),
            ),
            uiEvent = AccountUIEvent(
                onClearCredentialButtonClicked = {},
                onSubmitCredentials = { _, _, _ -> },
                onRefresh = {},
                onMeterSerialNumberSelected = { _, _ -> },
                onErrorShown = {},
                onClearCache = {},
                onScrolledToTop = {},
                onShowSnackbar = {},
                onSpecialErrorScreenShown = {},
            ),
        )
    }
}
