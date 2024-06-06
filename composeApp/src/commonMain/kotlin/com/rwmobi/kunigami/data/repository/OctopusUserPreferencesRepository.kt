/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.data.source.local.preferences.interfaces.PreferencesStore
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class OctopusUserPreferencesRepository(
    private val preferencesStore: PreferencesStore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserPreferencesRepository {
    override suspend fun isDemoMode(): Boolean {
        return withContext(dispatcher) {
            when {
                getApiKey() == null -> true
                getAccountNumber() == null -> true
                getMpan() == null -> true
                getMeterSerialNumber() == null -> true
                else -> false
            }
        }
    }

    override suspend fun getApiKey(): String? {
        return withContext(dispatcher) {
            preferencesStore.getStringData(key = PreferencesKeys.API_KEY.name)
        }
    }

    override suspend fun getAccountNumber(): String? {
        return withContext(dispatcher) {
            preferencesStore.getStringData(key = PreferencesKeys.ACCOUNT_NUMBER.name)
        }
    }

    override suspend fun getMpan(): String? {
        return withContext(dispatcher) {
            preferencesStore.getStringData(key = PreferencesKeys.MPAN.name)
        }
    }

    override suspend fun getMeterSerialNumber(): String? {
        return withContext(dispatcher) {
            preferencesStore.getStringData(key = PreferencesKeys.METER_SERIAL_NUMBER.name)
        }
    }

    /**
     * This is currently used by desktop only.
     */
    override suspend fun getWindowSize(): DpSize? {
        return withContext(dispatcher) {
            val width = preferencesStore.getFloatData(key = PreferencesKeys.WINDOW_WIDTH.name)
            val height = preferencesStore.getFloatData(key = PreferencesKeys.WINDOW_HEIGHT.name)

            if (width == null || height == null) {
                null
            } else {
                DpSize(width = width.dp, height = height.dp)
            }
        }
    }

    override suspend fun setApiKey(apiKey: String) {
        withContext(dispatcher) {
            preferencesStore.saveStringData(key = PreferencesKeys.API_KEY.name, value = apiKey)
        }
    }

    override suspend fun setAccountNumber(accountNumber: String) {
        withContext(dispatcher) {
            preferencesStore.saveStringData(key = PreferencesKeys.ACCOUNT_NUMBER.name, value = accountNumber)
        }
    }

    override suspend fun setMpan(mpan: String) {
        withContext(dispatcher) {
            preferencesStore.saveStringData(key = PreferencesKeys.MPAN.name, value = mpan)
        }
    }

    override suspend fun setMeterSerialNumber(meterSerialNumber: String) {
        withContext(dispatcher) {
            preferencesStore.saveStringData(key = PreferencesKeys.METER_SERIAL_NUMBER.name, value = meterSerialNumber)
        }
    }

    /**
     * This is currently used by desktop only.
     */
    override suspend fun setWindowSize(size: DpSize) {
        withContext(dispatcher) {
            preferencesStore.saveFloatData(key = PreferencesKeys.WINDOW_WIDTH.name, value = size.width.value)
            preferencesStore.saveFloatData(key = PreferencesKeys.WINDOW_HEIGHT.name, value = size.height.value)
        }
    }

    override suspend fun clearCredentials() {
        withContext(dispatcher) {
            listOf(
                PreferencesKeys.API_KEY,
                PreferencesKeys.ACCOUNT_NUMBER,
                PreferencesKeys.MPAN,
                PreferencesKeys.METER_SERIAL_NUMBER,
                PreferencesKeys.WINDOW_WIDTH,
                PreferencesKeys.WINDOW_HEIGHT,
            ).forEach {
                preferencesStore.removeData(key = it.name)
            }
        }
    }

    override suspend fun clearStorage() {
        withContext(dispatcher) {
            preferencesStore.clearAll()
        }
    }
}

private enum class PreferencesKeys {
    API_KEY,
    ACCOUNT_NUMBER,
    MPAN,
    METER_SERIAL_NUMBER,
    WINDOW_WIDTH,
    WINDOW_HEIGHT,
}
