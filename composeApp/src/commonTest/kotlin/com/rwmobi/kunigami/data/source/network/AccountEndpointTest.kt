/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network

import com.rwmobi.kunigami.data.source.network.restapi.AccountEndpoint
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.test.samples.GetAccountSampleData
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

@Suppress("TooManyFunctions")
class AccountEndpointTest {
    private val fakeBaseUrl = "https://some.fakeurl.com"
    private val fakeApiKey = "sk_live_xXxX1xx1xx1xx1XXxX1Xxx1x"
    private val fakeAccountNumber = "B-1234A1A1"

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

    // 🗂 getAccount
    @Test
    fun getAccount_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val accountEndpoint = AccountEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetAccountSampleData.json,
            ),
        )

        val result = accountEndpoint.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )
        assertEquals(GetAccountSampleData.dto, result)
    }

    @Test
    fun getAccount_ShouldReturnNull_WhenHttpStatusIsNotFound() = runTest {
        val accountEndpoint = AccountEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.NotFound,
                contentType = "text/json",
                payload = """{content:"not found"}""",
            ),
        )

        val result = accountEndpoint.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )
        assertNull(result)
    }

    @Test
    fun getAccount_ShouldThrowHttpException_WhenHttpStatusIsInternalServerError() = runTest {
        val accountEndpoint = AccountEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        try {
            accountEndpoint.getAccount(
                apiKey = fakeApiKey,
                accountNumber = fakeAccountNumber,
            )
            fail("Expected HttpException")
        } catch (e: HttpException) {
            // Successful if HttpException is thrown
        }
    }
}
