/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.composehelper

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
