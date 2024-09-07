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

package com.rwmobi.kunigami.domain.usecase.product

import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFilteredProductsUseCase(
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(postcode: String): Result<List<ProductSummary>> {
        return withContext(dispatcher) {
            val getProductsResult = octopusApiRepository.getProducts(postcode = postcode)
            getProductsResult.fold(
                onSuccess = {
                    getProductsResult.mapCatching { products ->
                        products
                            .filter {
                                it.direction == ProductDirection.IMPORT &&
                                    !it.features.contains(ProductFeature.BUSINESS) &&
                                    it.brand == "OCTOPUS_ENERGY" && // TODO: Only for RestApi
                                    !it.features.contains(ProductFeature.RESTRICTED) // TODO: Only for RestApi
                            }
                            .sortedBy { it.displayName }
                    }
                },
                onFailure = {
                    getProductsResult
                },
            )
        }
    }
}
