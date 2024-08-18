/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    onClick = {},
                )
            }
        }
    }
}
