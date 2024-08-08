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
import composeapp.kunigami.BuildConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single<Settings> { provideSettings(serviceName = BuildConfig.PACKAGE_NAME) }

    single<RoomDatabase.Builder<OctometerDatabase>> {
        val documentsDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )?.path ?: NSHomeDirectory()
        val dbFilePath = "$documentsDirectory/octometer_database.db"
        Room.databaseBuilder<OctometerDatabase>(
            name = dbFilePath,
        )
    }
}
