/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.data.source.network.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.kunigami.data.source.network.ProductsEndpoint
import com.rwmobi.kunigami.data.source.network.samples.GetAccountSampleData
import com.rwmobi.kunigami.domain.exceptions.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/***
 * This can be an integration test.
 * We provide MockEngine to real Endpoints instead of mocking endpoints.
 */
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
class OctopusRestApiRepositoryTest() {

    private lateinit var octopusRestApiRepository: OctopusRestApiRepository

    private val fakeBaseUrl = "https://some.fakeurl.com"
    private val fakeApiKey = "sk_live_xXxX1xx1xx1xx1XXxX1Xxx1x"
    private val fakeAccountNumber = "B-1234A1A1"

    private val mockEngineInternalServerError = MockEngine { _ ->
        respond(
            content = ByteReadChannel("Internal Server Error"),
            status = HttpStatusCode.InternalServerError,
            headers = headersOf(HttpHeaders.ContentType, "text/html"),
        )
    }

    private val mockEngineNotFound = MockEngine { _ ->
        respond(
            content = ByteReadChannel("""{content:"not found"}"""),
            status = HttpStatusCode.NotFound,
            headers = headersOf(HttpHeaders.ContentType, "text/json"),
        )
    }

    private fun setUpRepository(engine: MockEngine) {
        val client = HttpClient(engine = engine) {
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

        octopusRestApiRepository = OctopusRestApiRepository(
            productsEndpoint = ProductsEndpoint(
                baseUrl = fakeBaseUrl,
                httpClient = client,
                dispatcher = UnconfinedTestDispatcher(),
            ),
            electricityMeterPointsEndpoint = ElectricityMeterPointsEndpoint(
                baseUrl = fakeBaseUrl,
                httpClient = client,
                dispatcher = UnconfinedTestDispatcher(),
            ),
            accountEndpoint = AccountEndpoint(
                baseUrl = fakeBaseUrl,
                httpClient = client,
                dispatcher = UnconfinedTestDispatcher(),
            ),
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    // 🗂 getAccount
    @Test
    fun `getAccount should return expected domain model`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetAccountSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), GetAccountSampleData.account)
    }

    @Test
    fun `getAccount should return Success-null when data source gives null response`() = runTest {
        setUpRepository(engine = mockEngineNotFound)

        val result = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), null)
    }

    @Test
    fun `getAccount should return Failure when data source throws an exception`() = runTest {
        setUpRepository(engine = mockEngineInternalServerError)

        val result = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }
}
