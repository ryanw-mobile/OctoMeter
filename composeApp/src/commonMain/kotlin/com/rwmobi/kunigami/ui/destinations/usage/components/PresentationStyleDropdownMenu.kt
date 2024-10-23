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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
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
    val dimension = getScreenSizeInfo().getDimension()

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
    // Preview doesn't work for this component
    CommonPreviewSetup {
        PresentationStyleDropdownMenu(
            modifier = Modifier.fillMaxSize(),
            currentPresentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
            expanded = true,
            onDismiss = {},
            onSwitchPresentationStyle = {},
        )
    }
}
