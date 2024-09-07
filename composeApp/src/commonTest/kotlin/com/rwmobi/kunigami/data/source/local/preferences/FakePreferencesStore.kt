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
