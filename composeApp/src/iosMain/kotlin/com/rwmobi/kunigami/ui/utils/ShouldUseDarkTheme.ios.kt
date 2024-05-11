/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun shouldUseDarkTheme(): Boolean {
    val uiStyle = UIScreen.mainScreen.traitCollection.userInterfaceStyle
    return uiStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}
