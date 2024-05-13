/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class GetConsumptionUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<List<Consumption>> {
        return withContext(dispatcher) {
            runCatching {
                val apiKey = userPreferencesRepository.getApiKey()
                val mpan = userPreferencesRepository.getMpan()
                val meterSerialNumber = userPreferencesRepository.getMeterSerialNumber()

                requireNotNull(value = apiKey, lazyMessage = { "API Key is null" })
                requireNotNull(value = mpan, lazyMessage = { "MPAN is null" })
                requireNotNull(value = meterSerialNumber, lazyMessage = { "Meter Serial Number is null" })

                octopusRepository.getConsumption(
                    apiKey = apiKey,
                    mpan = mpan,
                    meterSerialNumber = meterSerialNumber,
                ).fold(
                    onSuccess = { consumption ->
                        consumption
                    },
                    onFailure = { throwable ->
                        throw throwable
                    },
                )
            }.except<CancellationException, _>()
        }
    }
}
