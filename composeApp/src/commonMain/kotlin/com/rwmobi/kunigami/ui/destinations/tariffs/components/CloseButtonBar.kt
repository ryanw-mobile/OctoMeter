/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.close_fill
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun CloseButtonBar(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onCloseClicked) {
            Icon(
                painter = painterResource(resource = Res.drawable.close_fill),
                contentDescription = "Dismiss this pane",
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        CloseButtonBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight),
            onCloseClicked = {},
        )
    }
}
