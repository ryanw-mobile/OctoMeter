/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.NoValidMeterException
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class InitialiseAccountUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        apiKey: String,
        accountNumber: String,
    ): Result<Unit> {
        return withContext(dispatcher) {
            runCatching {
                restApiRepository.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                ).fold(
                    onSuccess = { account ->
                        // Most users should have only 1 MPAN and 1 Meter in an account.
                        // We automatically pick the first MPAN and Meter Serial
                        // Users can change that on Account screen
                        if (account.isEmpty() ||
                            account[0].electricityMeterPoints.isEmpty() ||
                            account[0].electricityMeterPoints[0].meterSerialNumbers.isEmpty()
                        ) {
                            throw NoValidMeterException()
                        }

                        with(account.first()) {
                            userPreferencesRepository.setApiKey(apiKey = apiKey)
                            userPreferencesRepository.setAccountNumber(accountNumber = accountNumber)
                            userPreferencesRepository.setMpan(mpan = electricityMeterPoints[0].mpan)
                            userPreferencesRepository.setMeterSerialNumber(meterSerialNumber = electricityMeterPoints[0].meterSerialNumbers[0])
                        }
                        Unit
                    },
                    onFailure = { throwable ->
                        throw throwable
                    },
                )
            }.except<CancellationException, _>()
        }
    }
}
