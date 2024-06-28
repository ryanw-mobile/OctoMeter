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
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<Settings> { provideSettings(context = androidContext().applicationContext) }

    single<RoomDatabase.Builder<OctometerDatabase>> {
        val appContext = androidContext().applicationContext
        val dbFile = appContext.getDatabasePath("octometer_database")
        Room.databaseBuilder<OctometerDatabase>(
            context = appContext,
            name = dbFile.absolutePath,
        )
    }
}
