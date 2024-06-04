/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class GetConsumptionUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val demoRestApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    /***
     * Specify grouping, and start date, then use case will determine the right end date.
     */
    suspend operator fun invoke(
        periodFrom: Instant,
        periodTo: Instant,
        groupBy: ConsumptionDataGroup,
    ): Result<List<Consumption>> {
        return withContext(dispatcher) {
            runCatching {
                val isDemoMode = userPreferencesRepository.isDemoMode()

                if (!isDemoMode) {
                    val apiKey = userPreferencesRepository.getApiKey()
                    val mpan = userPreferencesRepository.getMpan()
                    val meterSerialNumber = userPreferencesRepository.getMeterSerialNumber()

                    requireNotNull(value = apiKey, lazyMessage = { "API Key is null" })
                    requireNotNull(value = mpan, lazyMessage = { "MPAN is null" })
                    requireNotNull(value = meterSerialNumber, lazyMessage = { "Meter Serial Number is null" })

                    restApiRepository.getConsumption(
                        apiKey = apiKey,
                        mpan = mpan,
                        meterSerialNumber = meterSerialNumber,
                        periodFrom = periodFrom,
                        periodTo = periodTo,
                        orderBy = ConsumptionDataOrder.PERIOD,
                        groupBy = groupBy,
                    ).fold(
                        onSuccess = { consumption ->
                            consumption.sortedBy {
                                it.intervalStart
                            }
                        },
                        onFailure = { throwable ->
                            throw throwable
                        },
                    )
                } else {
                    demoRestApiRepository.getConsumption(
                        apiKey = "",
                        mpan = "",
                        meterSerialNumber = "",
                        periodFrom = periodFrom,
                        periodTo = periodTo,
                        orderBy = ConsumptionDataOrder.PERIOD,
                        groupBy = groupBy,
                    ).fold(
                        onSuccess = { consumption ->
                            consumption.sortedBy {
                                it.intervalStart
                            }
                        },
                        onFailure = { throwable ->
                            throw throwable
                        },
                    )
                }
            }.except<CancellationException, _>()
        }
    }
}
