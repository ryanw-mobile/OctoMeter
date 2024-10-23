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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.destinations.account.components.AppInfoFooter
import com.rwmobi.kunigami.ui.destinations.account.components.CredentialsInputForm
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
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

@Composable
internal fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUIState,
    uiEvent: AccountUIEvent,
) {
    val dimension = getScreenSizeInfo().getDimension()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() },
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
                    requestedScreenType = AccountScreenType.Onboarding,
                    userProfile = null,
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
}
