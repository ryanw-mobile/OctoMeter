/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.source.network

import com.rwmobi.roctopus.data.source.network.dto.ProductsApiResponse
import com.rwmobi.roctopus.domain.exceptions.except
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ProductsEndpoint(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val baseUrl = "https://api.octopus.energy"
    private val endpointUrl = "$baseUrl/v1/products/"

    suspend fun getProducts(
        brand: String? = null,
        isVariable: Boolean? = null,
        isBusiness: Boolean? = null,
        availableAt: String? = null,
        isGreen: Boolean? = null,
        isPrepay: Boolean? = null,
    ): Result<ProductsApiResponse?> {
        return withContext(dispatcher) {
            Result.runCatching {
                val response: ProductsApiResponse? = httpClient.get(endpointUrl) {
                    parameter("brand", brand)
                    parameter("is_variable", isVariable)
                    parameter("is_business", isBusiness)
                    parameter("available_at", availableAt)
                    parameter("is_green", isGreen)
                    parameter("is_prepay", isPrepay)
                }.body()

                // TODO: return domain model, if success it will be empty list
                // response?.results
                response
            }.except<CancellationException, _>()
        }
    }
}
