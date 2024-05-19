/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.extensions.roundDownToHour
import com.rwmobi.kunigami.domain.model.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

class GetStandardUnitRateUseCase(
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val currentTime = Clock.System.now().roundDownToHour()

                restApiRepository.getStandardUnitRates(
                    productCode = "AGILE-FLEX-22-11-25",
                    tariffCode = "E-1R-AGILE-FLEX-22-11-25-J",
                    periodFrom = currentTime,
                    periodTo = currentTime.plus(duration = Duration.parse("1d")),
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
