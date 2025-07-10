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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.coin
import org.jetbrains.compose.resources.painterResource

@PreviewLightDark
@Composable
private fun Preview(
    modifier: Modifier = Modifier,
) {
    AppTheme {
        Surface(
            modifier = Modifier.wrapContentSize(),
        ) {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SquareButton(
                    modifier = Modifier
                        .width(width = 96.dp)
                        .wrapContentHeight(),
                    icon = painterResource(resource = Res.drawable.coin),
                    text = "Money Generator",
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = AppTheme.colorScheme.secondaryContainer,
                        contentColor = AppTheme.colorScheme.onSecondaryContainer,
                    ),
                    onClick = {},
                )

                SquareButton(
                    modifier = Modifier
                        .width(width = 96.dp)
                        .wrapContentHeight(),
                    icon = painterResource(resource = Res.drawable.coin),
                    text = "Money",
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = AppTheme.colorScheme.secondaryContainer,
                        contentColor = AppTheme.colorScheme.onSecondaryContainer,
                    ),
                    onClick = {},
                )
            }
        }
    }
}
