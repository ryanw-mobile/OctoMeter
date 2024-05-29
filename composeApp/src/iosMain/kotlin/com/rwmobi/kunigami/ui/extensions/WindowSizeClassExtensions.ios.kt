/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.rwmobi.kunigami.ui.model.PlatformType

actual fun WindowSizeClass.getPlatformType() = PlatformType.IOS
