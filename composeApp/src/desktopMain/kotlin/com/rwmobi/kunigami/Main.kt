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
import com.rwmobi.kunigami.di.graphQLModule
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

    // This has to be executed before koinApplication, so we cannot use stringResources
    val aboutAppDescription = "OctoMeter version %s (Build %s)\nMake the smartest use of your electricity\n\n%s"
    customizeMacOsAboutMenu(
        title = "About OctoMeter",
        message = aboutAppDescription.format(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.GITHUB_LINK),
    )

    // TODO: This looks strange but it works. Either later we don't use Koin or we improve it.
    koinApplication {
        printLogger(Level.ERROR)
        logger(KermitKoinLogger(Logger.withTag("koin")))
        modules(
            platformModule,
            dispatcherModule,
            viewModelModule,
            userCaseModule,
            ktorModule,
            graphQLModule,
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
            graphQLModule,
            repositoryModule,
            dataSourceModule,
        )
    }

    application {
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
