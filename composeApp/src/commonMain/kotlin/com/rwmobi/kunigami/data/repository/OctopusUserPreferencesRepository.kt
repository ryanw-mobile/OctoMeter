/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

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
            preferencesStore.getData(key = PreferencesKeys.API_KEY.name)
        }
    }

    override suspend fun getAccountNumber(): String? {
        return withContext(dispatcher) {
            preferencesStore.getData(key = PreferencesKeys.ACCOUNT_NUMBER.name)
        }
    }

    override suspend fun getMpan(): String? {
        return withContext(dispatcher) {
            preferencesStore.getData(key = PreferencesKeys.MPAN.name)
        }
    }

    override suspend fun getMeterSerialNumber(): String? {
        return withContext(dispatcher) {
            preferencesStore.getData(key = PreferencesKeys.METER_SERIAL_NUMBER.name)
        }
    }

    override suspend fun setApiKey(apiKey: String) {
        withContext(dispatcher) {
            preferencesStore.saveData(key = PreferencesKeys.API_KEY.name, value = apiKey)
        }
    }

    override suspend fun setAccountNumber(accountNumber: String) {
        withContext(dispatcher) {
            preferencesStore.saveData(key = PreferencesKeys.ACCOUNT_NUMBER.name, value = accountNumber)
        }
    }

    override suspend fun setMpan(mpan: String) {
        withContext(dispatcher) {
            preferencesStore.saveData(key = PreferencesKeys.MPAN.name, value = mpan)
        }
    }

    override suspend fun setMeterSerialNumber(meterSerialNumber: String) {
        withContext(dispatcher) {
            preferencesStore.saveData(key = PreferencesKeys.METER_SERIAL_NUMBER.name, value = meterSerialNumber)
        }
    }

    override suspend fun clearCredentials() {
        withContext(dispatcher) {
            listOf(
                PreferencesKeys.API_KEY,
                PreferencesKeys.ACCOUNT_NUMBER,
                PreferencesKeys.MPAN,
                PreferencesKeys.METER_SERIAL_NUMBER,
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
}
