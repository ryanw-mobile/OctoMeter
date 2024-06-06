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

    override suspend fun saveStringData(key: String, value: String) {
        withContext(dispatcher) {
            settings[key] = value
        }
    }

    override suspend fun getStringData(key: String): String? {
        return withContext(dispatcher) {
            settings[key]
        }
    }

    override suspend fun saveFloatData(key: String, value: Float) {
        withContext(dispatcher) {
            settings[key] = value
        }
    }

    override suspend fun getFloatData(key: String): Float? {
        return withContext(dispatcher) {
            settings[key]
        }
    }

    override suspend fun removeData(key: String) {
        return withContext(dispatcher) {
            settings.remove(key)
        }
    }

    override suspend fun clearAll() {
        return withContext(dispatcher) {
            settings.clear()
        }
    }
}
