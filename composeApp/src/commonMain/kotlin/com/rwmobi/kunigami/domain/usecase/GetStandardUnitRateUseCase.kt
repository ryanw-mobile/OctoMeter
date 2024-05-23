/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class GetStandardUnitRateUseCase(
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant,
        periodTo: Instant,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                restApiRepository.getStandardUnitRates(
                    productCode = productCode,
                    tariffCode = tariffCode,
                    periodFrom = periodFrom,
                    periodTo = periodTo,
                ).fold(
                    onSuccess = { rates ->
                        rates.sortedBy { it.validFrom }
                    },
                    onFailure = { throwable ->
                        throw throwable
                    },
                )
            }.except<CancellationException, _>()
        }
    }
}
