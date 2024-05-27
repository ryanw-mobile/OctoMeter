/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.circle_check
import kunigami.composeapp.generated.resources.selected
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PresentationStyleDropdownMenu(
    modifier: Modifier,
    expanded: Boolean,
    currentPresentationStyle: ConsumptionPresentationStyle,
    onDismiss: () -> Unit,
    onSwitchPresentationStyle: (consumptionPresentationStyle: ConsumptionPresentationStyle) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        offset = DpOffset(0.dp, dimension.grid_1),
        onDismissRequest = { onDismiss() },
    ) {
        ConsumptionPresentationStyle.entries.forEach { presentationStyle ->
            DropdownMenuItem(
                modifier = Modifier,
                enabled = currentPresentationStyle != presentationStyle,
                colors = MenuDefaults.itemColors().copy(
                    textColor = MaterialTheme.colorScheme.onSurface,
                ),
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                trailingIcon = {
                    if (currentPresentationStyle == presentationStyle) {
                        Icon(
                            modifier = Modifier.size(size = dimension.grid_3),
                            painter = painterResource(resource = Res.drawable.circle_check),
                            contentDescription = stringResource(resource = Res.string.selected),
                        )
                    }
                },
                onClick = { onSwitchPresentationStyle(presentationStyle) },
                text = {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = stringResource(resource = presentationStyle.stringResource),
                    )
                },
            )
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
                currentPresentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
                expanded = true,
                onDismiss = {},
                onSwitchPresentationStyle = {},
            )
        }
    }
}
