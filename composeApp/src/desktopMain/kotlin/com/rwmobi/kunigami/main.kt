/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rwmobi.kunigami.di.appModule
import com.rwmobi.kunigami.di.dataSourceModule
import com.rwmobi.kunigami.di.dispatcherModule
import com.rwmobi.kunigami.di.ktorModule
import com.rwmobi.kunigami.di.repositoryModule
import com.rwmobi.kunigami.di.userCaseModule
import com.rwmobi.kunigami.di.viewModelModule
import com.rwmobi.kunigami.ui.utils.customizeMacOsAboutMenu
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
        "OctoMeter",
    )

    application {
        customizeMacOsAboutMenu()
        startKoin {
            printLogger(Level.ERROR)
            modules(
                appModule,
                dispatcherModule,
                viewModelModule,
                userCaseModule,
                ktorModule,
                repositoryModule,
                dataSourceModule,
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
