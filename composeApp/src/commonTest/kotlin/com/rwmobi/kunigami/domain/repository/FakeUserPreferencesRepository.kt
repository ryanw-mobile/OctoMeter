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
