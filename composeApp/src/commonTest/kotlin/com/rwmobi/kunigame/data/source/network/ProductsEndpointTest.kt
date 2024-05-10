/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.data.source.network

import com.rwmobi.kunigame.data.source.network.samples.GetDayUnitRatesSampleData
import com.rwmobi.kunigame.data.source.network.samples.GetNightUnitRatesSampleData
import com.rwmobi.kunigame.data.source.network.samples.GetProductsSampleData
import com.rwmobi.kunigame.data.source.network.samples.GetStandardUnitRatesSampleData
import com.rwmobi.kunigame.data.source.network.samples.GetStandingChargesSampleData
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ProductsEndpointTest {
    private val fakeBaseUrl = "https://some.fakeurl.com"

    @OptIn(ExperimentalSerializationApi::class)
    private fun setupEngine(status: HttpStatusCode, contentType: String, payload: String): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(payload),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, contentType),
            )
        }

        return HttpClient(engine = mockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                        allowTrailingComma = true
                    },
                )
            }
        }
    }

    // 🗂 getProducts
    @Test
    fun getProducts_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetProductsSampleData.json,
            ),
        )

        val result = productsEndpoint.getProducts()
        result shouldBe GetProductsSampleData.dto
    }

    @Test
    fun getProducts_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        shouldThrowExactly<NoTransformationFoundException> {
            productsEndpoint.getProducts()
        }
    }

    // 🗂 getStandardUnitRates
    @Test
    fun getStandardUnitRates_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetStandardUnitRatesSampleData.json,
            ),
        )

        val result = productsEndpoint.getStandardUnitRates(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        result shouldBe GetStandardUnitRatesSampleData.dto
    }

    @Test
    fun getStandardUnitRates_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        shouldThrowExactly<NoTransformationFoundException> {
            productsEndpoint.getStandardUnitRates(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
        }
    }

    // 🗂 getStandingCharges
    @Test
    fun getStandingCharges_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetStandingChargesSampleData.json,
            ),
        )

        val result = productsEndpoint.getStandingCharges(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        result shouldBe GetStandingChargesSampleData.dto
    }

    @Test
    fun getStandingCharges_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        shouldThrowExactly<NoTransformationFoundException> {
            productsEndpoint.getStandingCharges(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
        }
    }

    // 🗂 getDayUnitRates
    @Test
    fun getDayUnitRates_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetDayUnitRatesSampleData.json,
            ),
        )

        val result = productsEndpoint.getDayUnitRates(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        result shouldBe GetDayUnitRatesSampleData.dto
    }

    @Test
    fun getDayUnitRates_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        shouldThrowExactly<NoTransformationFoundException> {
            productsEndpoint.getDayUnitRates(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
        }
    }

    // 🗂 getNightUnitRates
    @Test
    fun getNightUnitRates_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetNightUnitRatesSampleData.json,
            ),
        )

        val result = productsEndpoint.getNightUnitRates(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        result shouldBe GetNightUnitRatesSampleData.dto
    }

    @Test
    fun getNightUnitRates_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        shouldThrowExactly<NoTransformationFoundException> {
            productsEndpoint.getNightUnitRates(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
        }
    }
}
