/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import com.rwmobi.kunigami.data.source.local.preferences.interfaces.PreferencesStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MultiplatformPreferencesStore(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PreferencesStore {

    override suspend fun saveData(key: String, value: String) {
        withContext(dispatcher) {
            settings[key] = value
        }
    }

    override suspend fun getData(key: String): String? {
        return withContext(dispatcher) {
            settings[key]
        }
    }
}