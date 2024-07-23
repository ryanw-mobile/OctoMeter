/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
                val apiKey = userPreferencesRepository.getApiKey()
                val accountNumber = userPreferencesRepository.getAccountNumber()

                // isDemoMode should have rejected null cases. This is to avoid using !! below
                checkNotNull(value = apiKey, lazyMessage = { "Expect API Key but null" })
                checkNotNull(value = accountNumber, lazyMessage = { "Expect Account Number but null" })

                octopusApiRepository.getAccount(
                    apiKey = apiKey,
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
