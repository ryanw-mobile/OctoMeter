/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.source.network

import com.rwmobi.roctopus.data.source.network.dto.ProductsApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class ProductsEndpoint(
    baseUrl: String,
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val endpointUrl = "$baseUrl/v1/products/"

    suspend fun getProducts(
        brand: String? = null,
        isVariable: Boolean? = null,
        isBusiness: Boolean? = null,
        availableAt: String? = null,
        isGreen: Boolean? = null,
        isPrepay: Boolean? = null,
    ): ProductsApiResponse? {
        return withContext(dispatcher) {
            httpClient.get(endpointUrl) {
                parameter("brand", brand)
                parameter("is_variable", isVariable)
                parameter("is_business", isBusiness)
                parameter("available_at", availableAt)
                parameter("is_green", isGreen)
                parameter("is_prepay", isPrepay)
            }.body() as ProductsApiResponse?
        }
    }
}
