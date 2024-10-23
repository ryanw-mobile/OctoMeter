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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun LabelValueRow(
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    label: String?,
    value: String?,
) {
    val dimension = getScreenSizeInfo().getDimension()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        label?.let {
            Text(
                modifier = Modifier.weight(weight = 1f),
                style = style,
                fontWeight = FontWeight.Bold,
                text = it,
            )
        }

        value?.let {
            Text(
                style = style,
                text = it,
            )
        }
    }
}
