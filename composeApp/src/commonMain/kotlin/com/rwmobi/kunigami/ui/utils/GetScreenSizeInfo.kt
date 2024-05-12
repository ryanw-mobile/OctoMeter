/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import androidx.compose.runtime.Composable
import com.rwmobi.kunigami.ui.model.ScreenSizeInfo

@Composable
expect fun getScreenSizeInfo(): ScreenSizeInfo
