/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import composeapp.kunigami.BuildConfig
import java.awt.Desktop
import java.awt.Menu
import java.awt.MenuItem
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JOptionPane

internal fun customizeMacOsAboutMenu(
    title: String,
    message: String,
) {
    val desktop = Desktop.getDesktop()
    val aboutHandler = {
        val iconPath = "icons/ic_launcher_macos_64x64.png"
        val iconImage = loadImage(iconPath)
        val icon = if (iconImage != null) ImageIcon(iconImage) else null

        JOptionPane.showMessageDialog(
            null,
            "OctoMeter version ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})\nMake the smartest use of your electricity\n\n${BuildConfig.GITHUB_LINK}",
            "About OctoMeter",
            JOptionPane.INFORMATION_MESSAGE,
            icon,
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

private fun loadImage(path: String): BufferedImage? {
    return try {
        ImageIO.read(File(path))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
