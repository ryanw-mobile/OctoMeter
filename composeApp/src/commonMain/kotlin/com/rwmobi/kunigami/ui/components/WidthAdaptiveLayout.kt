/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.runtime.Composable

/***
 * A simple wrapper to indicate the layout supports both compact (default) or wide layout.
 */
@Composable
fun WidthAdaptiveLayout(
    useCompact: Boolean = true,
    compactLayout: @Composable () -> Unit = {},
    wideLayout: @Composable () -> Unit = {},
) {
    if (useCompact) {
        compactLayout()
    } else {
        wideLayout()
    }
}
