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
import com.rwmobi.kunigami.data.source.network.samples.GetTariffSampleData
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

/***
 * This can be an integration test.
 * We provide MockEngine to real Endpoints instead of mocking endpoints.
 */
@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
class OctopusRestApiRepositoryTest {

    private lateinit var octopusRestApiRepository: OctopusRestApiRepository

    private val fakeBaseUrl = "https://some.fakeurl.com"
    private val fakeApiKey = "sk_live_xXxX1xx1xx1xx1XXxX1Xxx1x"
    private val fakeAccountNumber = "B-1234A1A1"
    private val sampleTariffCode = "E-1R-AGILE-FLEX-22-11-25-A"

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

    // 🗂 getTariff
    @Test
    fun `getTariff should return expected domain model`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getTariff(
            tariffCode = sampleTariffCode,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetTariffSampleData.tariff, actual = result.getOrNull())
    }

    @Test
    fun `getTariff should return failure when product code cannot be resorlved`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getTariff(
            tariffCode = "invalid tariff code",
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getTariff should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getTariff(
            tariffCode = sampleTariffCode,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
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
        assertEquals(expected = GetAccountSampleData.account, actual = result.getOrNull())
    }

    @Test
    fun `getAccount should return Success-null when data source gives null response`() = runTest {
        setUpRepository(engine = mockEngineNotFound)

        val result = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isSuccess)
        assertNull(actual = result.getOrNull())
    }

    @Test
    fun `getAccount should return Failure when data source throws an exception`() = runTest {
        setUpRepository(engine = mockEngineInternalServerError)

        val result = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isFailure)
        assertTrue(actual = result.exceptionOrNull() is HttpException)
    }

    @Test
    fun `getAccount should return cached result when it hits the cache`() = runTest {
        var shouldEndpointReturnError = false
        setUpRepository(
            engine = MockEngine { _ ->
                if (!shouldEndpointReturnError) {
                    respond(
                        content = ByteReadChannel(GetAccountSampleData.json),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                } else {
                    respond(
                        content = ByteReadChannel("Internal Server Error"),
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf(HttpHeaders.ContentType, "text/html"),
                    )
                }
            },
        )

        val firstAccess = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )
        assertTrue(firstAccess.isSuccess)
        assertEquals(expected = GetAccountSampleData.account, actual = firstAccess.getOrNull())

        shouldEndpointReturnError = true
        val secondAccess = octopusRestApiRepository.getAccount(
            apiKey = fakeApiKey,
            accountNumber = fakeAccountNumber,
        )
        assertTrue(secondAccess.isSuccess)
        assertEquals(expected = GetAccountSampleData.account, actual = secondAccess.getOrNull())
    }
}
