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

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
internal fun ButtonTitleBar(
    modifier: Modifier = Modifier
        .background(color = AppTheme.colorScheme.secondary)
        .fillMaxWidth()
        .height(height = 56.dp),
    title: String,
    color: Color = AppTheme.colorScheme.onSecondaryContainer,
    leftButton: @Composable (() -> Unit)? = null,
    rightButton: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Center),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = title,
        )

        leftButton?.let {
            Box(modifier = Modifier.align(alignment = Alignment.CenterStart)) {
                it()
            }
        }

        rightButton?.let {
            Box(modifier = Modifier.align(alignment = Alignment.CenterEnd)) {
                it()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        ButtonTitleBar(
            modifier = Modifier
                .background(color = AppTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(height = AppTheme.dimens.minListItemHeight),
            title = "Sample Title",
        )
    }
}
