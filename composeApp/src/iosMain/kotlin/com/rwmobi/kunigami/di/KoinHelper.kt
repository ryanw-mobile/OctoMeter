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
package com.rwmobi.kunigami.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.withTag
import co.touchlab.kermit.koin.KermitKoinLogger
import org.koin.core.context.startKoin
import kotlin.time.measureTime

fun initKoin() {
    val timeTaken = measureTime {
        startKoin {
            logger(
                KermitKoinLogger(withTag("koin")),
            )
            modules(
                platformModule,
                dispatcherModule,
                viewModelModule,
                useCaseModule,
                graphQLModule,
                ktorModule,
                repositoryModule,
                dataSourceModule,
            )
        }
    }.inWholeMilliseconds

    Logger.v("initKoin") { "It took $timeTaken milliseconds" }
}
