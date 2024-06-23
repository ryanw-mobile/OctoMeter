/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.data.source.local.database.FakeDataBaseDataSource
import com.rwmobi.kunigami.data.source.network.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.kunigami.data.source.network.ProductsEndpoint
import com.rwmobi.kunigami.data.source.network.samples.GetAccountSampleData
import com.rwmobi.kunigami.data.source.network.samples.GetProductsSampleData
import com.rwmobi.kunigami.data.source.network.samples.GetTariffSampleData
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
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
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/***
 * This can be an integration test.
 * We test Repository with endpoint as a unit.
 * We provide MockEngine to real Endpoints instead of mocking endpoints just for the sake of strict unit tests.
 */
@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
class OctopusRestApiRepositoryTest {

    private lateinit var octopusRestApiRepository: OctopusRestApiRepository

    private val fakeBaseUrl = "https://some.fakeurl.com"
    private val fakeApiKey = "sk_live_xXxX1xx1xx1xx1XXxX1Xxx1x"
    private val fakeAccountNumber = "B-1234A1A1"
    private val fakeMpan = "9900000999999"
    private val fakeMeterSerialNumber = "99A9999999"
    private val sampleProductCode = "AGILE-FLEX-22-11-25"
    private val sampleTariffCode = "E-1R-AGILE-FLEX-22-11-25-A"
    private val now = Clock.System.now()
    private lateinit var fakeDataBaseDataSource: FakeDataBaseDataSource

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

    private val mockEngineProductPaging = MockEngine { httpRequestData ->
        val pageNumber = httpRequestData.url.parameters.get("page")
        when (pageNumber) {
            "1", null -> {
                respond(
                    content = ByteReadChannel(GetProductsSampleData.jsonPage1),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }

            "2" -> {
                respond(
                    content = ByteReadChannel(GetProductsSampleData.jsonPage2),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }

            else -> {
                respond(
                    content = ByteReadChannel("Internal Server Error"),
                    status = HttpStatusCode.InternalServerError,
                    headers = headersOf(HttpHeaders.ContentType, "text/html"),
                )
            }
        }
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

        fakeDataBaseDataSource = FakeDataBaseDataSource()
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
            databaseDataSource = fakeDataBaseDataSource,
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
    fun `getTariff should return failure when product code cannot be resolved`() = runTest {
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

    // 🗂 getProducts
    @Test
    fun `getProducts should return expected domain model`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetProductsSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(expected = GetProductsSampleData.productSummary, actual = result.getOrNull())
    }

    @Test
    fun `getProducts should loop to get the entire data set when backend indicates request can be resumed`() = runTest {
        setUpRepository(
            engine = mockEngineProductPaging,
        )

        val result = octopusRestApiRepository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(expected = GetProductsSampleData.productSummaryTwoPages, actual = result.getOrNull())
    }

    @Test
    fun `getProducts should return correct domain model when requestedPage is not null`() = runTest {
        setUpRepository(
            engine = mockEngineProductPaging,
        )

        val result = octopusRestApiRepository.getProducts(
            requestedPage = 2,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = listOf(GetProductsSampleData.productSummaryPage2), actual = result.getOrNull())
    }

    @Test
    fun `getProducts should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getProducts()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getProductDetails
    @Test
    fun `getProductDetails should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getProductDetails(
            productCode = sampleProductCode,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getStandardUnitRates
    @Test
    fun `getStandardUnitRates should return failure when product code cannot be resolved`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getStandardUnitRates(
            tariffCode = "invalid tariff code",
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getStandardUnitRates should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getStandardUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getStandingCharges
    @Test
    fun `getStandingCharges should return failure when product code cannot be resolved`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getStandingCharges(
            tariffCode = "invalid tariff code",
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getStandingCharges should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getStandingCharges(
            tariffCode = sampleTariffCode,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getDayUnitRates
    @Test
    fun `getDayUnitRates should return failure when product code cannot be resolved`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getDayUnitRates(
            tariffCode = "invalid tariff code",
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getDayUnitRates should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getDayUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getNightUnitRates
    @Test
    fun `getNightUnitRates should return failure when product code cannot be resolved`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusRestApiRepository.getNightUnitRates(
            tariffCode = "invalid tariff code",
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getNightUnitRates should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusRestApiRepository.getNightUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getConsumption
    @Test
    fun `getConsumption should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        fakeDataBaseDataSource.getConsumptionsResponse = emptyList()
        val result = octopusRestApiRepository.getConsumption(
            apiKey = fakeApiKey,
            mpan = fakeMpan,
            meterSerialNumber = fakeMeterSerialNumber,
            period = now..now,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
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
