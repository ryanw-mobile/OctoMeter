/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

interface UserPreferencesRepository {
    suspend fun isDemoMode(): Boolean
    suspend fun getApiKey(): String?
    suspend fun setApiKey(apiKey: String)
    suspend fun getAccountNumber(): String?
    suspend fun setAccountNumber(accountNumber: String)
    suspend fun getMpan(): String?
    suspend fun setMpan(mpan: String)
    suspend fun getMeterSerialNumber(): String?
    suspend fun setMeterSerialNumber(meterSerialNumber: String)
    suspend fun clearCredentials()
    suspend fun clearStorage()
}
