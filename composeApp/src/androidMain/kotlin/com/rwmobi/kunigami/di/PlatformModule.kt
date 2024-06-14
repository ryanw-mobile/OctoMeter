/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.russhwolf.settings.Settings
import com.rwmobi.kunigami.data.source.local.preferences.provideSettings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<Settings> { provideSettings(context = androidContext().applicationContext) }
}
