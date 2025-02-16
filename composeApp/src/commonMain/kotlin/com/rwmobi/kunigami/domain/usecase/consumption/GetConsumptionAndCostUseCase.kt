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

package com.rwmobi.kunigami.domain.usecase.consumption

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class GetConsumptionAndCostUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusApiRepository: OctopusApiRepository,
    private val demoOctopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    /***
     * Pull consumption data, then maps with the corresponding tariff and rate.
     * Currently cost calculation is limited to half-hourly only.
     * All other options will have null costs.
     * If the use case failed to retrieve the account and tariff but consumption,
     * it will still return the consumption but with null costs.
     */
    suspend operator fun invoke(
        userProfile: UserProfile,
        period: ClosedRange<Instant>,
        groupBy: ConsumptionTimeFrame,
    ): Result<List<ConsumptionWithCost>> {
        return withContext(dispatcher) {
            runCatching {
                val isDemoMode = userPreferencesRepository.isDemoMode()
                if (isDemoMode) {
                    generateDemoResponse(
                        period = period,
                        groupBy = groupBy,
                    )
                } else {
                    val apiKey = userPreferencesRepository.getApiKey()
                    val mpan = userProfile.selectedMpan
                    val accountNumber = userProfile.account.accountNumber
                    val deviceId = userProfile.getSelectedElectricityMeterPoint()
                        ?.meters?.firstOrNull {
                            it.serialNumber == userProfile.selectedMeterSerialNumber
                        }?.deviceId

                    requireNotNull(value = apiKey, lazyMessage = { "Assertion failed: API Key is null" })
                    requireNotNull(value = deviceId, lazyMessage = { "Assertion failed: deviceId is null" })

                    octopusApiRepository.getConsumption(
                        accountNumber = accountNumber,
                        deviceId = deviceId,
                        mpan = mpan,
                        period = period,
                        groupBy = groupBy,
                    ).fold(
                        onSuccess = { consumptionWithCost ->
                            consumptionWithCost.sortedBy {
                                it.consumption.interval.start
                            }
                        },
                        onFailure = { throw it },
                    )
                }
            }.except<CancellationException, _>()
        }
    }

    private suspend fun generateDemoResponse(period: ClosedRange<Instant>, groupBy: ConsumptionTimeFrame): List<ConsumptionWithCost> {
        return demoOctopusApiRepository.getConsumption(
            accountNumber = "",
            deviceId = "",
            mpan = "",
            period = period,
            groupBy = groupBy,
        ).fold(
            onSuccess = { consumption ->
                consumption.sortedBy {
                    it.consumption.interval.start
                }
            },
            onFailure = { throw it },
        )
    }
}
