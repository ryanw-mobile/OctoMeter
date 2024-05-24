/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class, ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.destinations.account.components.AppInfoFooter
import com.rwmobi.kunigami.ui.destinations.account.components.CredentialsInputForm
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.bulb
import kunigami.composeapp.generated.resources.onboarding_introduction_1
import kunigami.composeapp.generated.resources.onboarding_introduction_2
import kunigami.composeapp.generated.resources.onboarding_question_1
import kunigami.composeapp.generated.resources.onboarding_reminder_1
import kunigami.composeapp.generated.resources.onboarding_reminder_2
import kunigami.composeapp.generated.resources.onboarding_welcome_aboard
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
internal fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUIState,
    uiEvent: AccountUIEvent,
) {
    val dimension = LocalDensity.current.getDimension()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
//                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(this@pointerInput.windowToken, 0)
                    },
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        Text(
            style = MaterialTheme.typography.displaySmall,
            text = stringResource(resource = Res.string.onboarding_welcome_aboard),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(resource = Res.string.onboarding_introduction_1),
        )

        Image(
            modifier = Modifier
                .size(size = dimension.grid_6)
                .align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.bulb),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.onboarding_question_1),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(resource = Res.string.onboarding_introduction_2),
        )

        CredentialsInputForm(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = dimension.grid_2),
            isSubmitButtonEnabled = !uiState.isLoading,
            onSubmitCredentials = uiEvent.onSubmitCredentials,
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(resource = Res.string.onboarding_reminder_1),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(resource = Res.string.onboarding_reminder_2),
        )

        AppInfoFooter(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            AccountScreen(
                uiState = AccountUIState(
                    isLoading = false,
                    isDemoMode = true,
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
                            code = "E-1R-AGILE-FLEX-22-11-25-A",
                            fullName = "Octopus 12M Fixed April 2024 v1",
                            displayName = "Octopus 12M Fixed",
                            vatInclusiveUnitRate = 99.257,
                            vatInclusiveStandingCharge = 94.682,
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
}
