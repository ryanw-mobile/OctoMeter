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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
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
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                        .padding(bottom = 8.dp)
                        .focusRequester(focusRequester),
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
