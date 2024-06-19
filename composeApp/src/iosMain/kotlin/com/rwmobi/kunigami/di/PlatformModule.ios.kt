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
import com.rwmobi.kunigami.data.source.local.database.instantiateImpl
import com.rwmobi.kunigami.data.source.local.preferences.provideSettings
import composeapp.kunigami.BuildConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single<Settings> { provideSettings(serviceName = BuildConfig.PACKAGE_NAME) }

    single<RoomDatabase.Builder<OctometerDatabase>> {
        val dbFilePath = NSHomeDirectory() + "/octometer_database.db"
        Room.databaseBuilder<OctometerDatabase>(
            name = dbFilePath,
            factory = { OctometerDatabase::class.instantiateImpl() },
        )
    }
}
