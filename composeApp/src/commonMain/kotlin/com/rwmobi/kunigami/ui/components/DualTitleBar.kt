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

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
internal fun DualTitleBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {
    Surface {
        Column(
            modifier = modifier.padding(horizontal = AppTheme.dimens.grid_3),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = title,
            )

            subtitle?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.onSecondaryContainer.copy(
                        alpha = 0.68f,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        DualTitleBar(
            modifier = Modifier
                .background(color = AppTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(height = 64.dp),
            title = "Sample title",
            subtitle = "Sample subtitle",
        )
    }
}
