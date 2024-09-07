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

package com.rwmobi.kunigami.domain.usecase.account

import com.rwmobi.kunigami.domain.exceptions.NoValidMeterException
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class InitialiseAccountUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        apiKey: String,
        accountNumber: String,
    ): Result<Unit> {
        return withContext(dispatcher) {
            runCatching {
                userPreferencesRepository.setApiKey(apiKey = apiKey)

                octopusApiRepository.getAccount(
                    accountNumber = accountNumber,
                ).fold(
                    onSuccess = { account ->
                        // Most users should have only 1 MPAN and 1 Meter in an account.
                        // We automatically pick the first MPAN and Meter Serial
                        // Users can change that on Account screen
                        if (account == null || !account.hasValidMeter()) {
                            throw NoValidMeterException()
                        }

                        userPreferencesRepository.setAccountNumber(accountNumber = accountNumber)
                        userPreferencesRepository.setMpan(mpan = account.electricityMeterPoints[0].mpan)
                        userPreferencesRepository.setMeterSerialNumber(meterSerialNumber = account.electricityMeterPoints[0].meters[0].serialNumber)
                    },
                    onFailure = { throw it },
                )
            }.except<CancellationException, _>()
        }
    }
}
