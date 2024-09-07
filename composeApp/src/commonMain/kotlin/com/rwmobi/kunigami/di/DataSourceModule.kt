/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
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
