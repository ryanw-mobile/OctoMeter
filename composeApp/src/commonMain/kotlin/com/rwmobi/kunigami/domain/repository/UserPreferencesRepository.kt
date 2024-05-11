/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

interface UserPreferencesRepository {
    suspend fun isOnboardingShown(): Boolean
    suspend fun getApiKey(): String?
    suspend fun getAccountNumber(): String?
    suspend fun getMpan(): String?
    suspend fun getMeterSerialNumber(): String?
}
