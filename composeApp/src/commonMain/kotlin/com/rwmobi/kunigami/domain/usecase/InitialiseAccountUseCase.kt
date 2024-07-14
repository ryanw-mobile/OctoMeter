/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

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
                octopusApiRepository.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                ).fold(
                    onSuccess = { account ->
                        // Most users should have only 1 MPAN and 1 Meter in an account.
                        // We automatically pick the first MPAN and Meter Serial
                        // Users can change that on Account screen
                        if (account == null || !account.hasValidMeter()) {
                            throw NoValidMeterException()
                        }

                        userPreferencesRepository.setApiKey(apiKey = apiKey)
                        userPreferencesRepository.setAccountNumber(accountNumber = accountNumber)
                        userPreferencesRepository.setMpan(mpan = account.electricityMeterPoints[0].mpan)
                        userPreferencesRepository.setMeterSerialNumber(meterSerialNumber = account.electricityMeterPoints[0].meterSerialNumbers[0])
                    },
                    onFailure = { throw it },
                )
            }.except<CancellationException, _>()
        }
    }
}
