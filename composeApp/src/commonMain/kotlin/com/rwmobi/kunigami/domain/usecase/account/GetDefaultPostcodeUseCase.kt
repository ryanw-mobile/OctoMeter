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

import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDefaultPostcodeUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private val defaultPostcode = "WC1X 0ND"

    /***
     * Return user's postcode using best effort.
     * In any case we cannot resolve it (e.g. demo mode), we return the default postcode.
     * This is only good for suggesting a postcode for query.
     */
    suspend operator fun invoke(): String {
        return withContext(dispatcher) {
            if (userPreferencesRepository.isDemoMode()) {
                defaultPostcode
            } else {
                val accountNumber = userPreferencesRepository.getAccountNumber()

                // isDemoMode should have rejected null cases. This is to avoid using !! below
                checkNotNull(value = accountNumber, lazyMessage = { "Expect Account Number but null" })

                octopusApiRepository.getAccount(
                    accountNumber = accountNumber,
                ).fold(
                    onFailure = { throwable ->
                        Logger.e("GetDefaultPostcodeUseCase", throwable)
                        defaultPostcode
                    },

                    onSuccess = { account ->
                        account?.postcode ?: defaultPostcode
                    },
                )
            }
        }
    }
}
