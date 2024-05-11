/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ScrollbarMultiplatform(
    modifier: Modifier,
    enabled: Boolean,
    lazyListState: LazyListState,
    content: @Composable () -> Unit,
) {
    content()
}
