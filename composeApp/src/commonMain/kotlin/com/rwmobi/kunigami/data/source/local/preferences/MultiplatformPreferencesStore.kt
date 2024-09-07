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
