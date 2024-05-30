/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_update_api_key
import kunigami.composeapp.generated.resources.cancel
import kunigami.composeapp.generated.resources.onboarding_label_your_api_key
import kunigami.composeapp.generated.resources.onboarding_placeholder_api_key
import kunigami.composeapp.generated.resources.update
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdateApiKeyDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onUpdateAPIKey: (String) -> Unit,
) {
    var inputValue by rememberSaveable { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(resource = Res.string.account_update_api_key),
            )
        },
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = {
                        Text(text = stringResource(resource = Res.string.onboarding_label_your_api_key))
                    },
                    placeholder = {
                        Text(text = stringResource(resource = Res.string.onboarding_placeholder_api_key))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onUpdateAPIKey(initialValue.trim()) },
                enabled = inputValue.isNotBlank(),
            ) {
                Text(
                    text = stringResource(resource = Res.string.update),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
            ) {
                Text(
                    text = stringResource(resource = Res.string.cancel),
                )
            }
        },
    )
}
