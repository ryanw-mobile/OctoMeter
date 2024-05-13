/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components.koalaplot

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AxisLabel(
    modifier: Modifier = Modifier,
    label: String,
) {
    Text(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelMedium,
        overflow = TextOverflow.Clip,
        maxLines = 1,
        text = label,
    )
}
