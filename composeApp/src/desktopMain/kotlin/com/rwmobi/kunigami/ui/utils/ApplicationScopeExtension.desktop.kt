/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import androidx.compose.ui.window.ApplicationScope
import composeapp.kunigami.BuildConfig
import java.awt.Desktop
import java.awt.Menu
import java.awt.MenuItem
import javax.swing.JOptionPane

internal fun ApplicationScope.customizeMacOsAboutMenu() {
    val desktop = Desktop.getDesktop()
    val aboutHandler = {
        // Create and show a custom about dialog
        JOptionPane.showMessageDialog(
            null,
            "OctoMeter version ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})\nMake the smartest use of your electricity\n\n${BuildConfig.GITHUB_LINK}",
            "About OctoMeter",
            JOptionPane.INFORMATION_MESSAGE,
        )
    }

    if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
        desktop.setAboutHandler { aboutHandler() }
    }

    val menuBar = java.awt.MenuBar()
    val appMenu = Menu("Application")
    val aboutMenuItem = MenuItem("About OctoMeter")
    aboutMenuItem.addActionListener { aboutHandler() }
    appMenu.add(aboutMenuItem)
    menuBar.add(appMenu)

    java.awt.Frame.getFrames().forEach { it.menuBar = menuBar }
}
