/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase.account

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class SyncUserProfileUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<UserProfile?> {
        return withContext(dispatcher) {
            runCatching {
                if (userPreferencesRepository.isDemoMode()) {
                    throw IncompleteCredentialsException()
                }

                val accountNumber = userPreferencesRepository.getAccountNumber()

                // isDemoMode should have rejected null cases. This is to avoid using !! below
                checkNotNull(value = accountNumber, lazyMessage = { "Expect Account Number but null" })

                octopusApiRepository.getAccount(
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

                        val tariffs = mutableListOf<Tariff>()
                        account?.getTariffHistory(mpan = selectedMpan)?.forEach { tariffCode ->
                            val tariff = octopusApiRepository.getTariff(
                                tariffCode = tariffCode,
                            )
                            tariff.getOrNull()?.let { tariffs.add(it) }
                        }

                        // If any of the information is missing, we are not comfortable to proceed.
                        // Caller making use of UserProfile should consider activating demo mode.
                        if (selectedAccount != null &&
                            selectedMpan != null &&
                            selectedMeterSerialNumber != null &&
                            tariffs.isNotEmpty()
                        ) {
                            // We merge tariffs here, so repository can safely cache the account object
                            UserProfile(
                                selectedMpan = selectedMpan,
                                selectedMeterSerialNumber = selectedMeterSerialNumber,
                                account = selectedAccount,
                                tariffs = tariffs,
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
