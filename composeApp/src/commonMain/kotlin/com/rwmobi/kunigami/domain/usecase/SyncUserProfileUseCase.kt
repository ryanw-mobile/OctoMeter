/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class SyncUserProfileUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<UserProfile?> {
        return withContext(dispatcher) {
            runCatching {
                if (userPreferencesRepository.isDemoMode()) {
                    throw IncompleteCredentialsException()
                }

                val apiKey = userPreferencesRepository.getApiKey()
                val accountNumber = userPreferencesRepository.getAccountNumber()

                // isDemoMode should have rejected null cases. This is to avoid using !! below
                checkNotNull(value = apiKey, lazyMessage = { "Expect API Key but null" })
                checkNotNull(value = accountNumber, lazyMessage = { "Expect Account Number but null" })

                restApiRepository.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                ).fold(
                    onFailure = { throwable ->
                        throw throwable
                    },

                    onSuccess = { account ->
                        var selectedAccount: Account? = null
                        var selectedMpan = userPreferencesRepository.getMpan()
                        var selectedMeterSerialNumber = userPreferencesRepository.getMeterSerialNumber()

                        // If no MPAN and meter serial number is selected, we default to the first MPAN and Meter
                        if (selectedMpan != null && selectedMeterSerialNumber != null) {
                            // Validate existing preferred MPAN and Meter still exists
                            val matchingMeterPoint = account?.getElectricityMeterPoint(
                                mpan = selectedMpan,
                                meterSerialNumber = selectedMeterSerialNumber,
                            )
                            if (matchingMeterPoint != null) {
                                selectedAccount = account
                            }
                        } else {
                            selectedAccount = account
                            selectedMpan = account?.getDefaultMpan()?.also {
                                userPreferencesRepository.setMpan(it)
                            }
                            selectedMeterSerialNumber = account?.getDefaultMeterSerialNumber()?.also {
                                userPreferencesRepository.setMeterSerialNumber(it)
                            }
                        }

                        // If any of the information is missing, we are not comfortable to proceed.
                        // Caller making use of UserProfile should consider activating demo mode.
                        if (selectedAccount != null &&
                            selectedMpan != null &&
                            selectedMeterSerialNumber != null
                        ) {
                            UserProfile(
                                selectedMpan = selectedMpan,
                                selectedMeterSerialNumber = selectedMeterSerialNumber,
                                account = selectedAccount,
                            )
                        } else {
                            null
                        }
                    },
                )
            }.except<CancellationException, _>()
        }
    }
}
