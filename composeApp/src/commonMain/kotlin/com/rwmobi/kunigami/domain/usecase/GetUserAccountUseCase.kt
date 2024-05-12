/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.repository.OctopusRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class GetUserAccountUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusRepository: OctopusRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<Account> {
        return withContext(dispatcher) {
            runCatching {
                val apiKey = userPreferencesRepository.getApiKey()
                val accountNumber = userPreferencesRepository.getAccountNumber()

                requireNotNull(value = apiKey, lazyMessage = { "API Key is null" })
                requireNotNull(value = accountNumber, lazyMessage = { "Account Number is null" })

                octopusRepository.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                ).fold(
                    onSuccess = { account ->
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
