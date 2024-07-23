/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
fun EditPostcodeDialog(
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
                    label = {
                        Text(
                            text = stringResource(resource = Res.string.tariffs_postcode_dialog_label),
                        )
                    },
                    placeholder = {
                        Text(text = postcode)
                    },
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
                Text(
                    text = stringResource(resource = Res.string.update),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(resource = Res.string.cancel),
                )
            }
        },
    )

    LaunchedEffect(Unit) {
        inputFocusRequester.requestFocus()
    }
}
