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

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.onboarding_button_connect
import kunigami.composeapp.generated.resources.onboarding_getting_started
import kunigami.composeapp.generated.resources.onboarding_label_account
import kunigami.composeapp.generated.resources.onboarding_label_your_api_key
import kunigami.composeapp.generated.resources.onboarding_placeholder_account
import kunigami.composeapp.generated.resources.onboarding_placeholder_api_key
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CredentialsInputForm(
    modifier: Modifier = Modifier,
    isSubmitButtonEnabled: Boolean,
    onSubmitCredentials: (apiKey: String, accountNumber: String, stringResolver: suspend (resId: StringResource) -> String) -> Unit,
) {
    val dimension = getScreenSizeInfo().getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(all = dimension.grid_3),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val accountFocusRequester = FocusRequester()
        var apiKey by rememberSaveable { mutableStateOf("") }
        var account by rememberSaveable { mutableStateOf("") }

        Text(
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.onboarding_getting_started),
        )

        TextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = {
                Text(text = stringResource(resource = Res.string.onboarding_label_your_api_key))
            },
            placeholder = {
                Text(text = stringResource(resource = Res.string.onboarding_placeholder_api_key))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { accountFocusRequester.requestFocus() },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .focusRequester(focusRequester = FocusRequester()),
        )

        TextField(
            value = account,
            onValueChange = { account = it },
            label = {
                Text(text = stringResource(resource = Res.string.onboarding_label_account))
            },
            placeholder = {
                Text(text = stringResource(Res.string.onboarding_placeholder_account))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onSubmitCredentials(apiKey.trim(), account.trim(), { getString(resource = it) })
                },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(focusRequester = accountFocusRequester),
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                keyboardController?.hide()
                onSubmitCredentials(apiKey.trim(), account.trim(), { getString(resource = it) })
            },
            enabled = isSubmitButtonEnabled && apiKey.isNotBlank() && account.isNotBlank(),
        ) {
            Text(text = stringResource(resource = Res.string.onboarding_button_connect))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        CredentialsInputForm(
            isSubmitButtonEnabled = true,
            onSubmitCredentials = { _, _, _ -> },
        )
    }
}
