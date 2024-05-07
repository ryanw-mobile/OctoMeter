/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.rwmobi.roctopus.di.appModule
import com.rwmobi.roctopus.di.ktorModule
import com.rwmobi.roctopus.di.repositoryModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

fun main() = application {
    startKoin {
        printLogger(Level.ERROR)
        modules(
            appModule,
            ktorModule,
            repositoryModule,
        )
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Roctopus",
    ) {
        App()
    }
}
