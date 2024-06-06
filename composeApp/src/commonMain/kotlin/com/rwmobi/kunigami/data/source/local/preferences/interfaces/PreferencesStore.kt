/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.preferences.interfaces

interface PreferencesStore {
    suspend fun saveStringData(key: String, value: String)
    suspend fun getStringData(key: String): String?
    suspend fun removeData(key: String)
    suspend fun clearAll()
    suspend fun saveFloatData(key: String, value: Float)
    suspend fun getFloatData(key: String): Float?
}
