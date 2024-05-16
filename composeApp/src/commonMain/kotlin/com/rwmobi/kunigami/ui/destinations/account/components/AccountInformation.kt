/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Agreement
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.components.DefaultFailureRetryScreen
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenLayout
import com.rwmobi.kunigami.ui.destinations.account.AccountUIEvent
import com.rwmobi.kunigami.ui.destinations.account.AccountUIState
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.coin
import kunigami.composeapp.generated.resources.dashboard
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun AccountInformation(
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
                text = "Cannot retrieve your account details, try again?",
                icon = painterResource(resource = Res.drawable.coin),
                primaryButtonLabel = "retry",
                onPrimaryButtonClicked = uiEvent.onRefresh,
                secondaryButtonLabel = "Clear credentials and launch demo mode",
                onSecondaryButtonClicked = uiEvent.onClearCredentialButtonClicked,
            )
        } else {
            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            with(uiState.account) {
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    text = "Account $accountNumber",
                )

                Text("$fullAddress")

                movedInAt?.let {
                    Text("Moved in at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
                }

                movedOutAt?.let {
                    Text("Moved out at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
                }

                electricityMeterPoints.forEach { meterPoint ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimension.grid_2),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = dimension.grid_2),
                            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleLarge,
                                text = "MPAN: ${meterPoint.mpan}",
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = dimension.grid_1),
                                color = MaterialTheme.colorScheme.inverseSurface,
                            )

                            val tariff = uiState.tariff
                            if (tariff == null) {
                                Text("Could not retrieve your traiff. retry?")
                            } else {
                                BoxWithConstraints(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = dimension.grid_2),
                                ) {
                                    if (uiState.requestedLayout is AccountScreenLayout.Compact) {
                                        TariffLayoutCompact(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight(),
                                            tariff = tariff,
                                            currentAgreement = meterPoint.currentAgreement,
                                        )
                                    } else {
                                        TariffLayoutWide(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight(),
                                            tariff = tariff,
                                            currentAgreement = meterPoint.currentAgreement,
                                        )
                                    }
                                }
                            }

                            val meterSerialNumberFontStyle = if (uiState.requestedLayout is AccountScreenLayout.Compact) {
                                MaterialTheme.typography.labelMedium
                            } else {
                                MaterialTheme.typography.titleMedium
                            }
                            meterPoint.meterSerialNumbers.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = MaterialTheme.shapes.small)
                                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                                        .requiredHeight(height = dimension.minTouchTarget),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .padding(horizontal = dimension.grid_2)
                                            .size(size = dimension.grid_3),
                                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        painter = painterResource(resource = Res.drawable.dashboard),
                                        contentDescription = null,
                                    )

                                    Text(
                                        modifier = Modifier.weight(1f),
                                        overflow = TextOverflow.Ellipsis,
                                        style = meterSerialNumberFontStyle,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        text = "Serial: $it",
                                    )

                                    Text(
                                        modifier = Modifier.padding(horizontal = dimension.grid_2)
                                            .clip(shape = MaterialTheme.shapes.large)
                                            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                                            .padding(
                                                horizontal = dimension.grid_2,
                                                vertical = dimension.grid_1,
                                            ),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        text = "SELECTED",
                                    )
                                }

                                Spacer(modifier = Modifier.size(size = dimension.grid_1))
                            }
                        }
                    }
                }
            }
        }

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
private fun AccountInformationPreview() {
    AppTheme {
        Surface {
            AccountInformation(
                modifier = Modifier.padding(all = 32.dp),
                uiState = AccountUIState(
                    isLoading = false,
                    isDemoMode = false,
                    requestedLayout = AccountScreenLayout.WideWrapped,
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
                    onSubmitCredentials = {},
                    onRefresh = {},
                    onErrorShown = {},
                    onShowSnackbar = {},
                ),
            )
        }
    }
}
