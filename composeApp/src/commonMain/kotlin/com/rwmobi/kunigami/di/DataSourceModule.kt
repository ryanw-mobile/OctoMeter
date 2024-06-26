/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rwmobi.kunigami.data.source.local.cache.InMemoryCacheDataSource
import com.rwmobi.kunigami.data.source.local.database.OctometerDatabase
import com.rwmobi.kunigami.data.source.local.database.RoomDatabaseDataSource
import com.rwmobi.kunigami.data.source.local.database.interfaces.DatabaseDataSource
import com.rwmobi.kunigami.data.source.local.preferences.MultiplatformPreferencesStore
import com.rwmobi.kunigami.data.source.local.preferences.interfaces.PreferencesStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    factory<PreferencesStore> {
        MultiplatformPreferencesStore(
            settings = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }

    factory<DatabaseDataSource> {
        val database: OctometerDatabase = get()
        RoomDatabaseDataSource(
            consumptionDao = database.consumptionDao,
            rateDao = database.rateDao,
        )
    }

    single<OctometerDatabase> {
        val builder: RoomDatabase.Builder<OctometerDatabase> = get()
        builder
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<InMemoryCacheDataSource> {
        InMemoryCacheDataSource()
    }
}
