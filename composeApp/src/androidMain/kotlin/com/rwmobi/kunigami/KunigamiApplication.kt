/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami

import android.app.Application
import com.rwmobi.kunigami.di.appModule
import com.rwmobi.kunigami.di.dispatcherModule
import com.rwmobi.kunigami.di.ktorModule
import com.rwmobi.kunigami.di.repositoryModule
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
