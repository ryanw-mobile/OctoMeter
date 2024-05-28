/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network

import com.rwmobi.kunigami.data.source.network.dto.prices.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.products.ProductsApiResponse
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.domain.extensions.formatInstantWithoutSeconds
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

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
    ): ProductsApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/") {
                parameter("brand", brand)
                parameter("is_variable", isVariable)
                parameter("is_business", isBusiness)
                parameter("available_at", availableAt)
                parameter("is_green", isGreen)
                parameter("is_prepay", isPrepay)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as ProductsApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw Exception("Error: HTTP ${response.status}")
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
                    throw Exception("Error: HTTP ${response.status}")
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
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/standard-unit-rates") {
                parameter("period_from", periodFrom?.formatInstantWithoutSeconds())
                parameter("period_to", periodTo?.formatInstantWithoutSeconds())
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw Exception("Error: HTTP ${response.status}")
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
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/standing-charges") {
                parameter("period_from", periodFrom?.formatInstantWithoutSeconds())
                parameter("period_to", periodTo?.formatInstantWithoutSeconds())
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw Exception("Error: HTTP ${response.status}")
                }
            }
        }
    }

    suspend fun getDayUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/day-unit-rates") {
                parameter("period_from", periodFrom?.formatInstantWithoutSeconds())
                parameter("period_to", periodTo?.formatInstantWithoutSeconds())
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw Exception("Error: HTTP ${response.status}")
                }
            }
        }
    }

    suspend fun getNightUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
    ): PricesApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$productCode/electricity-tariffs/$tariffCode/night-unit-rates") {
                parameter("period_from", periodFrom?.formatInstantWithoutSeconds())
                parameter("period_to", periodTo?.formatInstantWithoutSeconds())
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as PricesApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw Exception("Error: HTTP ${response.status}")
                }
            }
        }
    }
}
