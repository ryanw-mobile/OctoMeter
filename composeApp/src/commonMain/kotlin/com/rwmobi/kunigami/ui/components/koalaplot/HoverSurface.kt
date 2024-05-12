/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components.koalaplot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HoverSurface(
    modifier: Modifier = Modifier,
    padding: Dp,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.padding(padding),
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.LightGray,
    ) {
        Box(
            modifier = Modifier.padding(padding),
        ) {
            content()
        }
    }
}
