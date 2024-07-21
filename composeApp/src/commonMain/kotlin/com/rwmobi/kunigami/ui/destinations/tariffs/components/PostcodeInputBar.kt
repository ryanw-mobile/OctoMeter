/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.pulse
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun PostcodeInputBar(
    modifier: Modifier = Modifier,
    postcode: String?,
    onUpdatePostcode: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()
    val keyboardController = LocalSoftwareKeyboardController.current
    val accountFocusRequester = FocusRequester()
    var postcodeInput by rememberSaveable { mutableStateOf(postcode) }
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight)
                .padding(horizontal = dimension.grid_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val queryStatusString = postcode ?: "Provide a valid UK post code to proceed"
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Postcode: $queryStatusString",
            )

            IconButton(
                onClick = { },
            ) {
                Icon(
                    painter = painterResource(resource = Res.drawable.pulse),
                    contentDescription = "update postcode",
                )
            }

//            TextField(
//                modifier = Modifier
//                    .width(width = 320.dp)
//                    .focusRequester(focusRequester = accountFocusRequester)
//                    .onFocusChanged { focusState ->
//                        isFocused = focusState.isFocused
//                    },
//                value = postcodeInput,
//                onValueChange = { postcodeInput = it },
//                label = {
//                    Text(text = "Postcode")
//                },
//                placeholder = {
//                    Text(text = stringResource(Res.string.onboarding_placeholder_account))
//                },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        keyboardController?.hide()
//                        onUpdatePostcode()
//                    },
//                ),
//                trailingIcon = {
//                    if (isFocused) {
//                        Icon(
//                            modifier = Modifier.clickable {
//                                keyboardController?.hide()
//                                onUpdatePostcode()
//                            },
//                            painter = painterResource(resource = Res.drawable.countdown_clock),
//                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
//                            contentDescription = null,
//                        )
//                    }
//                },
//            )

            Spacer(modifier = Modifier.weight(1f))
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        PostcodeInputBar(
            modifier = Modifier.fillMaxWidth(),
            postcode = "WC1X 0ND",
            onUpdatePostcode = { -> },

        )
    }
}
