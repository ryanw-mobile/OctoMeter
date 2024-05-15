/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account.components

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.onboarding_button_connect
import kunigami.composeapp.generated.resources.onboarding_getting_started
import kunigami.composeapp.generated.resources.onboarding_label_account
import kunigami.composeapp.generated.resources.onboarding_label_your_api_key
import kunigami.composeapp.generated.resources.onboarding_placeholder_account
import kunigami.composeapp.generated.resources.onboarding_placeholder_api_key
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CredentialsInputForm(
    modifier: Modifier = Modifier,
    onSubmitCredentials: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(all = dimension.grid_3),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val accountFocusRequester = FocusRequester()
        var apiKey by remember { mutableStateOf("") }
        var account by remember { mutableStateOf("") }

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
                    onSubmitCredentials()
                },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(focusRequester = accountFocusRequester),
        )

        Button(
            onClick = {
                keyboardController?.hide()
                onSubmitCredentials()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(resource = Res.string.onboarding_button_connect))
        }
    }
}
