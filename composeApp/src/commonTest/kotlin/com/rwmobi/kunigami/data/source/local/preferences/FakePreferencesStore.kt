/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.preferences

import com.rwmobi.kunigami.data.source.local.preferences.interfaces.PreferencesStore

class FakePreferencesStore : PreferencesStore {
    private val stringData = mutableMapOf<String, String>()
    private val floatData = mutableMapOf<String, Float>()

    override suspend fun saveStringData(key: String, value: String) {
        stringData[key] = value
    }

    override suspend fun getStringData(key: String): String? {
        return stringData[key]
    }

    override suspend fun removeData(key: String) {
        stringData.remove(key)
        floatData.remove(key)
    }

    override suspend fun clearAll() {
        stringData.clear()
        floatData.clear()
    }

    override suspend fun saveFloatData(key: String, value: Float) {
        floatData[key] = value
    }

    override suspend fun getFloatData(key: String): Float? {
        return floatData[key]
    }
}
