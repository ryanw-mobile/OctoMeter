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
    suspend fun getWindowSize(): DpSize?
    suspend fun setWindowSize(size: DpSize)
}
