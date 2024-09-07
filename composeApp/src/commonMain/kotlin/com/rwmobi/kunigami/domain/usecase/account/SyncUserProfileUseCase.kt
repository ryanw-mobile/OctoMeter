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

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.UserProfile
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

                        // If any of the information is missing, we are not comfortable to proceed.
                        // Caller making use of UserProfile should consider activating demo mode.
                        if (selectedAccount != null &&
                            selectedMpan != null &&
                            selectedMeterSerialNumber != null
                        ) {
                            // We merge tariffs here, so repository can safely cache the account object
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
