/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.data.source.local.preferences.MultiplatformPreferencesStore
import com.rwmobi.kunigami.data.source.local.preferences.interfaces.PreferencesStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    factory<PreferencesStore> {
        MultiplatformPreferencesStore(
            settings = get(),
            dispatcher = get(named("IODispatcher")),
        )
    }
}
