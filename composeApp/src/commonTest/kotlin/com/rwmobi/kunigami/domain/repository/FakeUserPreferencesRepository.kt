/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

import androidx.compose.ui.unit.DpSize

class FakeUserPreferencesRepository : UserPreferencesRepository {

    var demoMode: Boolean? = null
    var apiKey: String? = null
    var accountNumber: String? = null
    var mpan: String? = null
    var meterSerialNumber: String? = null
    var windowSize: DpSize? = null

    override suspend fun isDemoMode(): Boolean {
        return demoMode ?: throw RuntimeException("isDemoMode not defined")
    }

    override suspend fun getApiKey(): String? {
        return apiKey
    }

    override suspend fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    override suspend fun getAccountNumber(): String? {
        return accountNumber
    }

    override suspend fun setAccountNumber(accountNumber: String) {
        this.accountNumber = accountNumber
    }

    override suspend fun getMpan(): String? {
        return mpan
    }

    override suspend fun setMpan(mpan: String) {
        this.mpan = mpan
    }

    override suspend fun getMeterSerialNumber(): String? {
        return meterSerialNumber
    }

    override suspend fun setMeterSerialNumber(meterSerialNumber: String) {
        this.meterSerialNumber = meterSerialNumber
    }

    override suspend fun clearCredentials() {
        apiKey = null
        accountNumber = null
        mpan = null
        meterSerialNumber = null
        demoMode = true
    }

    override suspend fun clearStorage() {
        clearCredentials()
    }

    override suspend fun getWindowSize(): DpSize? {
        return windowSize
    }

    override suspend fun setWindowSize(size: DpSize) {
        windowSize = size
    }
}
