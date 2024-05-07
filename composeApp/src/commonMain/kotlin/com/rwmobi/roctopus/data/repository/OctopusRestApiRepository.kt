/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.repository

import com.rwmobi.roctopus.data.repository.mapper.toProduct
import com.rwmobi.roctopus.data.source.network.ProductsEndpoint
import com.rwmobi.roctopus.domain.exceptions.except
import com.rwmobi.roctopus.domain.model.Product
import com.rwmobi.roctopus.domain.repository.OctopusRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class OctopusRestApiRepository(
    private val productsEndpoint: ProductsEndpoint,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : OctopusRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return withContext(dispatcher) {
            runCatching {
                val productsDto = productsEndpoint.getProducts()
                productsDto?.results?.toProduct() ?: emptyList()
            }.except<CancellationException, _>()
        }
    }
}
