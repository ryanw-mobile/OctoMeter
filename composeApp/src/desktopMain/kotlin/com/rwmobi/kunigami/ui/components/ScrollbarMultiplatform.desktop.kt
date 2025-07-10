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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.foundation.v2.maxScrollOffset
import androidx.compose.material.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    scrollState: ScrollState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    ScrollbarMultiplatform(
        modifier = modifier,
        enabled = enabled,
        scrollbarAdapter = rememberScrollbarAdapter(scrollState = scrollState),
        content = content,
    )
}

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    lazyListState: LazyListState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    ScrollbarMultiplatform(
        modifier = modifier,
        enabled = enabled,
        scrollbarAdapter = rememberScrollbarAdapter(scrollState = lazyListState),
        content = content,
    )
}

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    lazyGridState: LazyGridState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    ScrollbarMultiplatform(
        modifier = modifier,
        enabled = enabled,
        scrollbarAdapter = rememberScrollbarAdapter(scrollState = lazyGridState),
        content = content,
    )
}

@Composable
private fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    scrollbarAdapter: ScrollbarAdapter,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    val density = LocalDensity.current

    var isScrollbarVisible by remember { mutableStateOf(false) }

    // Debounce mechanism to stabilize the scrollbar visibility state
    val dimensGrid3 = AppTheme.dimens.grid_3
    LaunchedEffect(scrollbarAdapter.maxScrollOffset) {
        with(density) {
            if (scrollbarAdapter.maxScrollOffset > dimensGrid3.roundToPx()) {
                isScrollbarVisible = true
            } else {
                delay(1000) // Adjust the delay as needed
                if (scrollbarAdapter.maxScrollOffset == 0.0) {
                    isScrollbarVisible = false
                }
            }
        }
    }

    Row(
        modifier = modifier,
    ) {
        content(Modifier.weight(weight = 1f))

        AnimatedVisibility(visible = enabled && isScrollbarVisible) {
            Row {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = AppTheme.colorScheme.surfaceContainerHighest,
                )

                VerticalScrollbar(
                    adapter = scrollbarAdapter,
                    style = LocalScrollbarStyle.current.copy(
                        thickness = AppTheme.dimens.grid_1,
                        unhoverColor = AppTheme.colorScheme.outlineVariant.copy(
                            alpha = 0.5f,
                        ),
                        hoverColor = AppTheme.colorScheme.outline.copy(
                            alpha = 0.5f,
                        ),
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = AppTheme.colorScheme.surface)
                        .padding(horizontal = AppTheme.dimens.grid_0_5),
                )
            }
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
