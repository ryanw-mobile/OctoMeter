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

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.rwmobi.kunigami.ui.extensions.formatPostcode
import com.rwmobi.kunigami.ui.extensions.isValidPostcodePattern
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.cancel
import kunigami.composeapp.generated.resources.close_fill
import kunigami.composeapp.generated.resources.content_description_clear
import kunigami.composeapp.generated.resources.tariffs_postcode_dialog_label
import kunigami.composeapp.generated.resources.update
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PostcodeEditDialog(
    postcode: String,
    onUpdatePostcode: (newPostcode: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var editPostcode by remember { mutableStateOf(TextFieldValue("")) }
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val inputFocusRequester = FocusRequester()

    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequester = inputFocusRequester)
                        .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                    value = editPostcode,
                    onValueChange = { newValue ->
                        val cleanedText = newValue.text.filter { it.isLetterOrDigit() || it == ' ' }.uppercase()
                        editPostcode = newValue.copy(
                            text = cleanedText,
                            selection = TextRange(cleanedText.length),
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    singleLine = true,
                    label = { Text(text = stringResource(resource = Res.string.tariffs_postcode_dialog_label)) },
                    placeholder = { Text(text = postcode) },
                    trailingIcon = {
                        if (editPostcode.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    editPostcode = TextFieldValue("")
                                    inputFocusRequester.requestFocus()
                                },
                            ) {
                                Icon(
                                    painter = painterResource(resource = Res.drawable.close_fill),
                                    contentDescription = stringResource(resource = Res.string.content_description_clear),
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onUpdatePostcode(editPostcode.text.formatPostcode())
                        },
                    ),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onUpdatePostcode(editPostcode.text.formatPostcode()) },
                enabled = editPostcode.text.isValidPostcodePattern(),
            ) {
                Text(text = stringResource(resource = Res.string.update))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(resource = Res.string.cancel))
            }
        },
    )

    LaunchedEffect(Unit) {
        inputFocusRequester.requestFocus()
    }
}
