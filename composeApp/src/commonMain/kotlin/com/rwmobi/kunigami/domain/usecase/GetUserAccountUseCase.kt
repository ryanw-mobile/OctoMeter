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
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class GetUserAccountUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<Account> {
        return withContext(dispatcher) {
            runCatching {
                if (userPreferencesRepository.isDemoMode()) {
                    throw IncompleteCredentialsException()
                }

                val apiKey = userPreferencesRepository.getApiKey()
                val accountNumber = userPreferencesRepository.getAccountNumber()

                // This is to avoid using !! below
                checkNotNull(value = apiKey, lazyMessage = { "Expect API Key but null" })
                checkNotNull(value = accountNumber, lazyMessage = { "Expect Account Number but null" })

                restApiRepository.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                ).fold(
                    onSuccess = { account ->
                        // Each account number should map to one Account,
                        // We can't control the API, so we set to take only the first result
                        account.first()
                    },
                    onFailure = { throwable ->
                        throw throwable
                    },
                )
            }.except<CancellationException, _>()
        }
    }
}
