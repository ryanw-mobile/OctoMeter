/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame

import android.app.Application
import com.rwmobi.kunigame.di.appModule
import com.rwmobi.kunigame.di.dispatcherModule
import com.rwmobi.kunigame.di.ktorModule
import com.rwmobi.kunigame.di.repositoryModule
import com.rwmobi.kunigame.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class KunigameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@KunigameApplication)
            modules(
                appModule,
                dispatcherModule,
                viewModelModule,
                ktorModule,
                repositoryModule,
            )
        }
    }
}
