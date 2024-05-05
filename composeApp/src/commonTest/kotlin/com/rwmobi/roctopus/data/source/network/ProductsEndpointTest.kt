/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.source.network

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
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

    @Test
    fun getProducts_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val productsEndpoint = ProductsEndpoint(
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = ProductsEndpointSampleData.json,
            ),
        )

        val result = productsEndpoint.getProducts()
        result.isSuccess shouldBe true
        result.getOrNull() shouldBe ProductsEndpointSampleData.dto
    }

    @Test
    fun getProducts_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val productsEndpoint = ProductsEndpoint(
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        val result = productsEndpoint.getProducts()
        result.isFailure shouldBe true
        result.exceptionOrNull()!!.shouldBeTypeOf<NoTransformationFoundException>()
    }
}
