/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class GetConsumptionAndCostUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val demoRestApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    /***
     * Pull consumption data, then maps with the corresponding tariff and rate.
     * Currently cost calculation is limited to half-hourly only.
     * All other options will have null costs.
     */
    suspend operator fun invoke(
        periodFrom: Instant,
        periodTo: Instant,
        groupBy: ConsumptionTimeFrame,
    ): Result<List<ConsumptionWithCost>> {
        return withContext(dispatcher) {
            runCatching {
                val isDemoMode = userPreferencesRepository.isDemoMode()

                if (!isDemoMode) {
                    val apiKey = userPreferencesRepository.getApiKey()
                    val mpan = userPreferencesRepository.getMpan()
                    val meterSerialNumber = userPreferencesRepository.getMeterSerialNumber()

                    requireNotNull(value = apiKey, lazyMessage = { "Assertion failed: API Key is null" })
                    requireNotNull(value = mpan, lazyMessage = { "Assertion failed: MPAN is null" })
                    requireNotNull(value = meterSerialNumber, lazyMessage = { "Assertion failed: Meter Serial Number is null" })

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
                            }.map {
                                ConsumptionWithCost(
                                    consumption = it,
                                    vatInclusiveCost = null,
                                )
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
                            }.map {
                                ConsumptionWithCost(
                                    consumption = it,
                                    vatInclusiveCost = null,
                                )
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
