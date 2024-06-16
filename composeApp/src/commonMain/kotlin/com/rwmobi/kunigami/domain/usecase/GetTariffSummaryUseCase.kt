/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/***
 * TODO: Handle multiple tariffs
 */
class GetTariffSummaryUseCase(
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(tariffCode: String): Result<TariffSummary> {
        return withContext(dispatcher) {
            restApiRepository.getSimpleProductTariff(
                productCode = TariffSummary.extractProductCode(tariffCode = tariffCode) ?: "",
                tariffCode = tariffCode,
            )
        }
    }
}
