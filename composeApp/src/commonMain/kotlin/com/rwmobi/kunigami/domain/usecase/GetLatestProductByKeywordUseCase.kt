/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLatestProductByKeywordUseCase(
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(keyword: String): String? {
        return withContext(dispatcher) {
            val getProductsResult = restApiRepository.getProducts()
            getProductsResult.fold(
                onSuccess = { products ->
                    val productCode = products.sortedByDescending {
                        it.availability.start
                    }.firstOrNull {
                        it.code.contains(keyword) &&
                            it.brand == "OCTOPUS_ENERGY"
                    }?.code
                    productCode
                },
                onFailure = {
                    Logger.e("GetLatestProductByKeywordUseCase", throwable = it)
                    null
                },
            )
        }
    }
}
