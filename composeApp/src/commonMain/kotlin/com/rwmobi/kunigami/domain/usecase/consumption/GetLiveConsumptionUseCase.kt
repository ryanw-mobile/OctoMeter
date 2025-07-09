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
import com.rwmobi.kunigami.domain.model.consumption.LiveConsumption
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.Duration

class GetLiveConsumptionUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    /**
     * There is no guarantee something will be returned, but we are
     * interested only in the live reading.
     */
    suspend operator fun invoke(
        meterDeviceId: String,
    ): Result<LiveConsumption?> = withContext(dispatcher) {
        runCatching {
            val isDemoMode = userPreferencesRepository.isDemoMode()

            if (isDemoMode) {
                null
            } else {
                val end = Clock.System.now()
                val start = end - Duration.parse("1m")

                octopusApiRepository.getSmartMeterLiveConsumption(
                    meterDeviceId = meterDeviceId,
                    start = start,
                    end = end,
                ).fold(
                    onSuccess = { consumption ->
                        consumption.maxByOrNull {
                            it.readAt
                        }
                    },
                    onFailure = { throw it },
                )
            }
        }.except<CancellationException, _>()
    }
}
