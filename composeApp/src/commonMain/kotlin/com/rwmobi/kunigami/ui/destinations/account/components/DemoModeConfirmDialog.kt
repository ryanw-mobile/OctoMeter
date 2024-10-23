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

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_credential_description
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.account_demo_mode
import kunigami.composeapp.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DemoModeConfirmDialog(
    onSwitchToDemoMode: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = stringResource(resource = Res.string.account_clear_credential_title)) },
        text = { Text(text = stringResource(resource = Res.string.account_clear_credential_description)) },
        confirmButton = {
            TextButton(onClick = onSwitchToDemoMode) { Text(stringResource(resource = Res.string.account_demo_mode)) }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text(stringResource(resource = Res.string.cancel)) }
        },
    )
}
