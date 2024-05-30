/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun LabelValueRow(
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    label: String?,
    value: String?,
) {
    val dimension = LocalDensity.current.getDimension()
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
