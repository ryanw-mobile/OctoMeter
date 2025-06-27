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

import android.app.Application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.rwmobi.kunigami.di.dataSourceModule
import com.rwmobi.kunigami.di.dispatcherModule
import com.rwmobi.kunigami.di.graphQLModule
import com.rwmobi.kunigami.di.ktorModule
import com.rwmobi.kunigami.di.platformModule
import com.rwmobi.kunigami.di.repositoryModule
import com.rwmobi.kunigami.di.useCaseModule
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
                useCaseModule,
                graphQLModule,
                ktorModule,
                repositoryModule,
            )
        }
    }
}
