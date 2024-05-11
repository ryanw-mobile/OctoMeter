/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.domain.model.ProductDirection
import com.rwmobi.kunigami.domain.model.ProductFeature
import com.rwmobi.kunigami.domain.repository.OctopusRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFilteredProductsUseCase(
    private val octopusRepository: OctopusRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return withContext(dispatcher) {
            val getProductsResult = octopusRepository.getProducts()

            if (getProductsResult.isFailure) {
                getProductsResult
            } else {
                getProductsResult.mapCatching { products ->
                    products.filter {
                        it.direction == ProductDirection.IMPORT &&
                            it.brand == "OCTOPUS_ENERGY" &&
                            !it.features.contains(ProductFeature.BUSINESS) &&
                            !it.features.contains(ProductFeature.RESTRICTED)
                    }
                }
            }
        }
    }
}
