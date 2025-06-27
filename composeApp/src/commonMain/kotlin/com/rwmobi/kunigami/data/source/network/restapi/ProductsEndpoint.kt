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

package com.rwmobi.kunigami.data.source.network.restapi

import com.rwmobi.kunigami.data.source.network.dto.prices.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.products.ProductsApiResponse
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.extensions.toIso8601WithoutSeconds
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Instant

class ProductsEndpoint(
    baseUrl: String,
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val endpointUrl = "$baseUrl/v1/products"

    suspend fun getProducts(
        brand: String? = null,
        isVariable: Boolean? = null,
        isBusiness: Boolean? = null,
        availableAt: String? = null,
        isGreen: Boolean? = null,
        isPrepay: Boolean? = null,
        page: Int? = null,
    ): ProductsApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/") {
                parameter("brand", brand)
                parameter("is_variable", isVariable)
                parameter("is_business", isBusiness)
                parameter("available_at", availableAt)
                parameter("is_green", isGreen)
                parameter("is_prepay", isPrepay)
                parameter("page", page)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as ProductsApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw HttpException(response.status.value)
                }
            }
        }
    }

    suspend fun getProduct(
        productCode: String,
    ): SingleProductApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/")

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as SingleProductApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw HttpException(response.status.value)
                }
            }
        }
    }

    // Agile prices, Go prices only differs by productCode and tariffCode
    suspend fun getStandardUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
        page: Int? = null,
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/standard-unit-rates") {
                parameter("period_from", periodFrom?.toIso8601WithoutSeconds())
                parameter("period_to", periodTo?.toIso8601WithoutSeconds())
                parameter("page", page)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw HttpException(response.status.value)
                }
            }
        }
    }

    // Fixed price contracts / standard variable price contracts / standing charges
    suspend fun getStandingCharges(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
        page: Int? = null,
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/standing-charges") {
                parameter("period_from", periodFrom?.toIso8601WithoutSeconds())
                parameter("period_to", periodTo?.toIso8601WithoutSeconds())
                parameter("page", page)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw HttpException(response.status.value)
                }
            }
        }
    }

    suspend fun getDayUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
        page: Int? = null,
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/day-unit-rates") {
                parameter("period_from", periodFrom?.toIso8601WithoutSeconds())
                parameter("period_to", periodTo?.toIso8601WithoutSeconds())
                parameter("page", page)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw HttpException(response.status.value)
                }
            }
        }
    }

    suspend fun getNightUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
        page: Int? = null,
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/night-unit-rates") {
                parameter("period_from", periodFrom?.toIso8601WithoutSeconds())
                parameter("period_to", periodTo?.toIso8601WithoutSeconds())
                parameter("page", page)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw HttpException(response.status.value)
                }
            }
        }
    }
}
