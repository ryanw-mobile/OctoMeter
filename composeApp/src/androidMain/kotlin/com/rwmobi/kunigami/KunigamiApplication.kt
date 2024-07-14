/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami

import android.app.Application
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
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class KunigamiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@KunigamiApplication)
            logger(
                KermitKoinLogger(Logger.withTag("koin")),
            )
            modules(
                platformModule,
                dispatcherModule,
                viewModelModule,
                dataSourceModule,
                userCaseModule,
                graphQLModule,
                ktorModule,
                repositoryModule,
            )
        }
    }
}
