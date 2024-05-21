/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
fun LargeTitleWithIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    label: String,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        Icon(
            modifier = Modifier.size(size = dimension.grid_3),
            tint = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.32f,
            ),
            painter = icon,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            text = label,
        )
    }
}
