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
