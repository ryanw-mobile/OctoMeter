/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.preferences.interfaces

interface PreferencesStore {
    suspend fun saveData(key: String, value: String)
    suspend fun getData(key: String): String?
    suspend fun removeData(key: String)
    suspend fun clearAll()
}
