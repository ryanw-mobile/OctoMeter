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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.SquareButton
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_cache
import kunigami.composeapp.generated.resources.account_demo_mode
import kunigami.composeapp.generated.resources.account_update_api_key
import kunigami.composeapp.generated.resources.database_remove_outline
import kunigami.composeapp.generated.resources.eraser
import kunigami.composeapp.generated.resources.key
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountOperationButtonBar(
    onUpdateApiKey: () -> Unit,
    onClearCache: () -> Unit,
    onSwitchToDemoMode: () -> Unit,
) {
    val dimension = getScreenSizeInfo().getDimension()
    var shouldShowDemoModeConfirmDialog by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(
            dimension.grid_4,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        SquareButton(
            modifier = Modifier
                .width(width = 80.dp)
                .wrapContentHeight(),
            icon = painterResource(resource = Res.drawable.key),
            text = stringResource(resource = Res.string.account_update_api_key),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            onClick = onUpdateApiKey,
        )

        SquareButton(
            modifier = Modifier
                .width(width = 80.dp)
                .wrapContentHeight(),
            icon = painterResource(resource = Res.drawable.database_remove_outline),
            text = stringResource(resource = Res.string.account_clear_cache),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            onClick = onClearCache,
        )

        SquareButton(
            modifier = Modifier
                .width(width = 80.dp)
                .wrapContentHeight(),
            icon = painterResource(resource = Res.drawable.eraser),
            text = stringResource(resource = Res.string.account_demo_mode),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            onClick = { shouldShowDemoModeConfirmDialog = true },
        )
    }

    if (shouldShowDemoModeConfirmDialog) {
        DemoModeConfirmDialog(
            onSwitchToDemoMode = onSwitchToDemoMode,
            onCancel = { shouldShowDemoModeConfirmDialog = false },
        )
    }
}
