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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
fun WidgetCard(
    modifier: Modifier = Modifier,
    heading: String? = null,
    contents: @Composable () -> Unit,
    footer: @Composable (() -> Unit)? = null,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .sizeIn(
                minWidth = dimension.windowWidthMedium / 2,
                maxWidth = dimension.windowWidthCompact,
            )
            .fillMaxSize()
            .padding(all = dimension.grid_2),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        heading?.let { headingText ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                text = headingText,
            )
        }

        Spacer(modifier = Modifier.weight(weight = 1f))

        contents()

        Spacer(modifier = Modifier.weight(weight = 1f))

        footer?.let { it() }
    }
}
