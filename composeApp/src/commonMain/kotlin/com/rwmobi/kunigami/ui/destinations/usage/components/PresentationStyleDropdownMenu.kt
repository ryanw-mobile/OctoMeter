/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.rwmobi.kunigami.ui.model.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PresentationStyleDropdownMenu(
    modifier: Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSwitchPresentationStyle: (consumptionPresentationStyle: ConsumptionPresentationStyle) -> Unit,
) {
    Column(
        modifier = modifier.background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onDismiss() },
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        DropdownMenu(
            modifier = Modifier
                .wrapContentSize()
                .background(color = MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { onDismiss() },
        ) {
            ConsumptionPresentationStyle.entries.forEach { presentationStyle ->
                DropdownMenuItem(
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = { onSwitchPresentationStyle(presentationStyle) },
                    text = {
                        Text(
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = presentationStyle.name,
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            PresentationStyleDropdownMenu(
                modifier = Modifier.fillMaxSize(),
                expanded = true,
                onDismiss = {},
                onSwitchPresentationStyle = {},
            )
        }
    }
}
