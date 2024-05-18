/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

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
    iconPath: String = "icons/ic_launcher_macos_64x64.png",
) {
    val desktop = Desktop.getDesktop()
    val aboutHandler = {
        val iconImage = loadImage(iconPath)
        val icon = if (iconImage != null) ImageIcon(iconImage) else null

        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE,
            icon,
        )
    }

    if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
        desktop.setAboutHandler { aboutHandler() }
    }

    val menuBar = java.awt.MenuBar()
    val appMenu = Menu("Application")
    val aboutMenuItem = MenuItem(title)
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
