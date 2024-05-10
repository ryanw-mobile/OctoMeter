/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.roctopus.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            appModule,
            dispatcherModule,
            viewModelModule,
            ktorModule,
            repositoryModule,
        )
    }
}
