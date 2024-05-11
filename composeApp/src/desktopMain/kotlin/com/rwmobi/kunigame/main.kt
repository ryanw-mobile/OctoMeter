/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rwmobi.kunigame.di.appModule
import com.rwmobi.kunigame.di.dispatcherModule
import com.rwmobi.kunigame.di.ktorModule
import com.rwmobi.kunigame.di.repositoryModule
import com.rwmobi.kunigame.di.viewModelModule
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import java.awt.Dimension

@OptIn(ExperimentalResourceApi::class)
fun main() {
    System.setProperty(
        "apple.awt.application.name",
        "Project Kunigame",
    )

    application {
        startKoin {
            printLogger(Level.ERROR)
            modules(
                appModule,
                dispatcherModule,
                viewModelModule,
                ktorModule,
                repositoryModule,
            )
        }

        val windowState = rememberWindowState(width = 800.dp, height = 560.dp)

        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(resource = Res.string.app_name),
            state = windowState,
        ) {
            window.minimumSize = Dimension(480, 480)

            App()
        }
    }
}
