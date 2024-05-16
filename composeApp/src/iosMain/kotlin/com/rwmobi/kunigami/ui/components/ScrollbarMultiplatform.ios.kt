/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    scrollState: ScrollState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    content(Modifier)
}

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    lazyListState: LazyListState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    content(Modifier)
}

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    lazyGridState: LazyGridState,
    content: @Composable (contentModifier: Modifier) -> Unit,
) {
    content(Modifier)
}
