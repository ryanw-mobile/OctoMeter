/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase.product

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class GetStandardUnitRateUseCase(
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        tariffCode: String,
        period: ClosedRange<Instant>,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                octopusApiRepository.getStandardUnitRates(
                    tariffCode = tariffCode,
                    period = period,
                ).fold(
                    onSuccess = { rates ->
                        rates.sortedBy { it.validity.start }
                    },
                    onFailure = { throwable ->
                        throw throwable
                    },
                )
            }.except<CancellationException, _>()
        }
    }
}
