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

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.coin

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            IconTextButton(
                icon = org.jetbrains.compose.resources.painterResource(resource = Res.drawable.coin),
                text = "Money Generator",
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = AppTheme.colorScheme.onPrimaryContainer,
                ),
                onClick = {},
            )

            IconTextButton(
                icon = org.jetbrains.compose.resources.painterResource(resource = Res.drawable.coin),
                text = "Money Generator",
                shape = AppTheme.shapes.large,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = AppTheme.colorScheme.secondaryContainer,
                    contentColor = AppTheme.colorScheme.onSecondaryContainer,
                ),
                onClick = {},
            )
        }
    }
}
