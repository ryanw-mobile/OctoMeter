/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OctopusUserPreferencesRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UserPreferencesRepository {
    override suspend fun isOnboardingShown(): Boolean {
        return false
    }

    override suspend fun getApiKey(): String? {
        return null
    }

    override suspend fun getAccountNumber(): String? {
        return null
    }

    override suspend fun getMpan(): String? {
        return null
    }

    override suspend fun getMeterSerialNumber(): String? {
        return null
    }
}
