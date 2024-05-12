/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    lazyListState: LazyListState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier,
    ) {
        content(Modifier.weight(weight = 1f))

        val scrollbarAdapter = rememberScrollbarAdapter(scrollState = lazyListState)
        if (enabled) {
            VerticalScrollbar(
                adapter = scrollbarAdapter,
                style = LocalScrollbarStyle.current.copy(
                    thickness = dimension.grid_1,
                    unhoverColor = MaterialTheme.colorScheme.secondaryContainer,
                    hoverColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    .padding(horizontal = dimension.grid_0_5),
            )
        }
    }
}

@Composable
@Preview
private fun ScrollbarMultiplatformPreview() {
    AppTheme {
        val lazyListState = rememberLazyListState()
        ScrollbarMultiplatform(
            lazyListState = lazyListState,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
            ) {
                repeat(20) {
                    item {
                        Text(
                            modifier = Modifier.height(height = 48.dp),
                            text = "Sample item",
                        )
                    }
                }
            }
        }
    }
}
