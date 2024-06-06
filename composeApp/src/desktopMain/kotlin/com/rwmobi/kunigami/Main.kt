/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.rwmobi.kunigami.di.dataSourceModule
import com.rwmobi.kunigami.di.dispatcherModule
import com.rwmobi.kunigami.di.ktorModule
import com.rwmobi.kunigami.di.platformModule
import com.rwmobi.kunigami.di.repositoryModule
import com.rwmobi.kunigami.di.userCaseModule
import com.rwmobi.kunigami.di.viewModelModule
import com.rwmobi.kunigami.ui.composehelper.collectAsStateMultiplatform
import com.rwmobi.kunigami.ui.composehelper.customizeMacOsAboutMenu
import com.rwmobi.kunigami.ui.viewmodels.PlatformMainViewModel
import composeapp.kunigami.BuildConfig
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.about_app_description
import kunigami.composeapp.generated.resources.about_app_title
import kunigami.composeapp.generated.resources.app_name
import kunigami.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication
import java.awt.Dimension

fun main() {
    System.setProperty(
        "apple.awt.application.name",
        "OctoMeter",
    )

    koinApplication {
        printLogger(Level.ERROR)
        logger(KermitKoinLogger(Logger.withTag("koin")))
        modules(
            platformModule,
            dispatcherModule,
            viewModelModule,
            userCaseModule,
            ktorModule,
            repositoryModule,
            dataSourceModule,
        )
    }

    startKoin {
        modules(
            platformModule,
            dispatcherModule,
            viewModelModule,
            userCaseModule,
            ktorModule,
            repositoryModule,
            dataSourceModule,
        )
    }

    application {
        customizeMacOsAboutMenu(
            title = stringResource(resource = Res.string.about_app_title),
            message = stringResource(resource = Res.string.about_app_description, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.GITHUB_LINK),
        )

        val viewModel: PlatformMainViewModel = koinInject()
        val preferredWindowSize by viewModel.windowSize.collectAsStateMultiplatform()
        val windowState = rememberWindowState(size = preferredWindowSize)

        Window(
            onCloseRequest = {
                viewModel.cachePreferredWindowSize(size = windowState.size)
                exitApplication()
            },
            title = stringResource(resource = Res.string.app_name),
            icon = painterResource(resource = Res.drawable.ic_launcher),
            state = windowState,
        ) {
            window.minimumSize = Dimension(480, 480)
            App()
        }

        LaunchedEffect(preferredWindowSize) {
            windowState.size = preferredWindowSize
        }
    }
}
