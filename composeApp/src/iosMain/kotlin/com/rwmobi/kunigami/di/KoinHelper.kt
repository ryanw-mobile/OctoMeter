/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.di

import co.touchlab.kermit.Logger
import org.koin.core.context.startKoin
import kotlin.time.measureTime

fun initKoin() {
    val timeTaken = measureTime {
        startKoin {
            modules(
                appModule,
                dispatcherModule,
                viewModelModule,
                userCaseModule,
                ktorModule,
                repositoryModule,
            )
        }
    }.inWholeMilliseconds

    Logger.v("initKoin") { "It took $timeTaken milliseconds" }
}
