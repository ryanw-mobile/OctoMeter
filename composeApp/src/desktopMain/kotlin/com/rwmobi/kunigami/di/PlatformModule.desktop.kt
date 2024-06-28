/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.russhwolf.settings.Settings
import com.rwmobi.kunigami.data.source.local.database.OctometerDatabase
import com.rwmobi.kunigami.data.source.local.preferences.provideSettings
import com.rwmobi.kunigami.ui.viewmodels.PlatformMainViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val platformModule = module {
    single<HttpClientEngine> { CIO.create() }
    single<Settings> { provideSettings() }
    factory {
        PlatformMainViewModel(
            userPreferencesRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    single<RoomDatabase.Builder<OctometerDatabase>> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "octometer_database.db")
        Room.databaseBuilder<OctometerDatabase>(
            name = dbFile.absolutePath,
        )
    }
}
