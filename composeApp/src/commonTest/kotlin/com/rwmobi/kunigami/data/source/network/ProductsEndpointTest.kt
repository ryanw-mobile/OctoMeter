/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network

import com.rwmobi.kunigami.data.source.network.restapi.ProductsEndpoint
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.test.samples.GetDayUnitRatesSampleData
import com.rwmobi.kunigami.test.samples.GetNightUnitRatesSampleData
import com.rwmobi.kunigami.test.samples.GetProductSampleData
import com.rwmobi.kunigami.test.samples.GetProductsSampleData
import com.rwmobi.kunigami.test.samples.GetStandardUnitRatesSampleData
import com.rwmobi.kunigami.test.samples.GetStandingChargesSampleData
import io.ktor.client.HttpClient
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
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

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
        assertEquals(GetProductsSampleData.dto, result)
    }

    @Test
    fun getProducts_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = productsEndpoint.getProducts()
        assertNull(result)
    }

    @Test
    fun getProducts_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            productsEndpoint.getProducts()
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
        }
    }

    // 🗂 getProduct
    @Test
    fun getProductWithSampleA_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetProductSampleData.json_var_22_11_01,
            ),
        )

        val result = productsEndpoint.getProduct(productCode = "sample-product-code")
        assertEquals(GetProductSampleData.dto_var_22_11_01, result)
    }

    @Test
    fun getProductWithSampleA_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = productsEndpoint.getProduct(productCode = "sample-product-code")
        assertNull(result)
    }

    @Test
    fun getProductWithSampleB_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetProductSampleData.json_agile_flex_22_11_25,
            ),
        )

        val result = productsEndpoint.getProduct(productCode = "sample-product-code")
        assertEquals(GetProductSampleData.dto_agile_flex_22_11_25, result)
    }

    @Test
    fun getProductWithSampleC_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetProductSampleData.json_oe_fix_12m_24_04_11,
            ),
        )

        val result = productsEndpoint.getProduct(productCode = "sample-product-code")
        assertEquals(GetProductSampleData.dto_oe_fix_12m_24_04_11, result)
    }

    @Test
    fun getProduct_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            productsEndpoint.getProduct(productCode = "sample-product-code")
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
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
        assertEquals(GetStandardUnitRatesSampleData.dto, result)
    }

    @Test
    fun getStandardUnitRates_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = productsEndpoint.getStandardUnitRates(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        assertNull(result)
    }

    @Test
    fun getStandardUnitRates_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            productsEndpoint.getStandardUnitRates(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
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
                payload = GetStandingChargesSampleData.var221101Json,
            ),
        )

        val result = productsEndpoint.getStandingCharges(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        assertEquals(GetStandingChargesSampleData.var221101Dto, result)
    }

    @Test
    fun getStandingCharges_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = productsEndpoint.getStandingCharges(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        assertNull(result)
    }

    @Test
    fun getStandingCharges_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            productsEndpoint.getStandingCharges(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
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
        assertEquals(GetDayUnitRatesSampleData.dto, result)
    }

    @Test
    fun getDayUnitRates_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = productsEndpoint.getDayUnitRates(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        assertNull(result)
    }

    @Test
    fun getDayUnitRates_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            productsEndpoint.getDayUnitRates(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
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
        assertEquals(GetNightUnitRatesSampleData.dto, result)
    }

    @Test
    fun getNightUnitRates_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = productsEndpoint.getNightUnitRates(
            productCode = "fake-product-code",
            tariffCode = "fake-tariff-code",
        )
        assertNull(result)
    }

    @Test
    fun getNightUnitRates_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            productsEndpoint.getNightUnitRates(
                productCode = "fake-product-code",
                tariffCode = "fake-tariff-code",
            )
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
        }
    }
}
