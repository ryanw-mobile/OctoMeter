/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.russhwolf.settings.Settings
import com.rwmobi.kunigami.data.source.local.preferences.provideSettings
import composeapp.kunigami.BuildConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

val appModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single<Settings> { provideSettings(serviceName = BuildConfig.PACKAGE_NAME) }
}
