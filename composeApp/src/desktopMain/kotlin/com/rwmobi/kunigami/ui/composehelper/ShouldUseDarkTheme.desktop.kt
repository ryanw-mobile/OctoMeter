/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.composehelper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jthemedetecor.OsThemeDetector
import javax.swing.SwingUtilities

@Composable
actual fun shouldUseDarkTheme(): Boolean {
    val detector: OsThemeDetector = OsThemeDetector.getDetector()
    var isSystemInDarkTheme by remember { mutableStateOf(detector.isDark) }
    detector.registerListener { isDark: Boolean ->
        SwingUtilities.invokeLater {
            isSystemInDarkTheme = isDark
        }
    }
    return isSystemInDarkTheme
}
