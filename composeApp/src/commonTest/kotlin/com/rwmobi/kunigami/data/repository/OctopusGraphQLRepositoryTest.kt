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

package com.rwmobi.kunigami.data.repository

import com.apollographql.adapter.datetime.KotlinxInstantAdapter
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.mockserver.MockResponse
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.enqueueString
import com.rwmobi.kunigami.data.source.local.cache.InMemoryCacheDataSource
import com.rwmobi.kunigami.data.source.local.database.FakeDataBaseDataSource
import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.network.graphql.ApolloGraphQLEndpoint
import com.rwmobi.kunigami.data.source.network.restapi.ProductsEndpoint
import com.rwmobi.kunigami.domain.exceptions.HttpException
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.graphql.type.DateTime
import com.rwmobi.kunigami.test.samples.GetAccountSampleData
import com.rwmobi.kunigami.test.samples.GetConsumptionSampleData
import com.rwmobi.kunigami.test.samples.GetProductsSampleData
import com.rwmobi.kunigami.test.samples.GetStandardUnitRatesSampleData
import com.rwmobi.kunigami.test.samples.GetStandingChargesSampleData
import com.rwmobi.kunigami.test.samples.GetTariffSampleData
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
import kotlinx.datetime.Instant
import kotlinx.io.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration

/***
 * This can be an integration test.
 * We test Repository with endpoint as a unit.
 * We provide MockEngine to real Endpoints instead of mocking endpoints just for the sake of strict unit tests.
 */
@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
class OctopusGraphQLRepositoryTest {

    private lateinit var octopusGraphQLRepository: OctopusGraphQLRepository

    private val samplePostcode = "WC1X 0ND"
    private val fakeBaseUrl = "https://some.fakeurl.com"
    private val fakeAccountNumber = "B-1234A1A1"
    private val fakeMpan = "9900000999999"
    private val fakeDeviceId = "00-00-00-00-00-00-00-00"
    private val fakeMeterSerialNumber = "99A9999999"
    private val sampleProductCode = "AGILE-FLEX-22-11-25"
    private val sampleTariffCode = "E-1R-AGILE-FLEX-22-11-25-C"
    private val now = Clock.System.now()
    private lateinit var mockServer: MockServer
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

    private val mockResponseInternalServerError = MockResponse.Builder()
        .statusCode(statusCode = 500)
        .headers(mapOf("Content-Length" to "0"))
        .body(body = "Internal server error")
        .delayMillis(0)
        .build()

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

    private suspend fun setUpRepository(engine: MockEngine) {
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
        val apolloClient = ApolloClient.Builder()
            .serverUrl(mockServer.url())
            .addCustomScalarAdapter(DateTime.type, KotlinxInstantAdapter)
            .build()
        val dispatcher = UnconfinedTestDispatcher()

        fakeDataBaseDataSource = FakeDataBaseDataSource()
        octopusGraphQLRepository = OctopusGraphQLRepository(
            productsEndpoint = ProductsEndpoint(
                baseUrl = fakeBaseUrl,
                httpClient = client,
                dispatcher = dispatcher,
            ),
            inMemoryCacheDataSource = InMemoryCacheDataSource(),
            databaseDataSource = fakeDataBaseDataSource,
            graphQLEndpoint = ApolloGraphQLEndpoint(
                apolloClient = apolloClient,
                dispatcher = dispatcher,
            ),
            dispatcher = dispatcher,
        )
    }

    @BeforeTest
    fun setupMockServer() {
        mockServer = MockServer()
    }

    @AfterTest
    fun cleanupMockServer() {
        mockServer.close()
    }

    // 🗂 getTariff
    @Test
    fun `getTariff should return expected domain model`() = runTest {
        mockServer.enqueueString(GetTariffSampleData.singleEnergyProductQueryResponse)
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetTariffSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusGraphQLRepository.getTariff(
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

        val result = octopusGraphQLRepository.getTariff(
            tariffCode = "invalid tariff code",
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getTariff should return failure when data source throws an exception`() = runTest {
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusGraphQLRepository.getTariff(
            tariffCode = sampleTariffCode,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApolloHttpException)
    }

    // 🗂 getProducts
    @Test
    fun `getProducts should return expected domain model`() = runTest {
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponse)
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetProductsSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusGraphQLRepository.getProducts(postcode = samplePostcode)

        assertTrue(result.isSuccess)
        assertEquals(expected = GetProductsSampleData.productSummary, actual = result.getOrNull())
    }

    @Test
    fun `getProducts should loop to get the entire data set when backend indicates request can be resumed`() = runTest {
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponsePage1)
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponsePage2)
        setUpRepository(
            engine = mockEngineProductPaging,
        )

        val result = octopusGraphQLRepository.getProducts(postcode = samplePostcode)

        assertTrue(result.isSuccess)
        assertEquals(expected = GetProductsSampleData.productSummaryTwoPages, actual = result.getOrNull())
    }

    @Test
    fun `getProducts should return the cached product list when available`() = runTest {
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponsePage1)
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponsePage2)
        setUpRepository(
            engine = mockEngineProductPaging,
        )
        octopusGraphQLRepository.getProducts(postcode = samplePostcode)
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)

        val result = octopusGraphQLRepository.getProducts(postcode = samplePostcode)

        assertTrue(result.isSuccess)
        assertEquals(expected = GetProductsSampleData.productSummaryTwoPages, actual = result.getOrNull())
    }

    @Test
    fun `getProducts should ignore the cached product list when requested postcode has changed`() = runTest {
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponsePage1)
        mockServer.enqueueString(GetProductsSampleData.energyProductsQueryResponsePage2)
        setUpRepository(
            engine = mockEngineProductPaging,
        )
        octopusGraphQLRepository.getProducts(postcode = samplePostcode)
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)

        val result = octopusGraphQLRepository.getProducts(postcode = "some-other-postcode")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApolloHttpException)
    }

    @Test
    fun `getProducts should return failure when data source throws an exception`() = runTest {
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusGraphQLRepository.getProducts(postcode = samplePostcode)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApolloHttpException)
    }

    // 🗂 getProductDetails
    @Test
    fun `getProductDetails should return failure when data source throws an exception`() = runTest {
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusGraphQLRepository.getProductDetails(
            productCode = sampleProductCode,
            postcode = samplePostcode,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApolloHttpException)
    }

    // 🗂 getStandardUnitRates
    @Test
    fun `getStandardUnitRates should return failure when product code cannot be resolved`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetStandardUnitRatesSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusGraphQLRepository.getStandardUnitRates(
            tariffCode = "invalid tariff code",
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getStandardUnitRates should return cached data when local database contains the required data set`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getRatesResponse = GetStandardUnitRatesSampleData.rateEntity

        val result = octopusGraphQLRepository.getStandardUnitRates(
            tariffCode = GetStandardUnitRatesSampleData.rateEntity[0].tariffCode,
            period = Instant.parse("2024-05-07T20:30:00Z")..Instant.parse("2024-05-07T22:00:00Z"),
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetStandardUnitRatesSampleData.rate, actual = result.getOrNull())
    }

    @Test
    fun `getStandardUnitRates should get data from remote data source when local database contains incomplete data set`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetStandardUnitRatesSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )
        fakeDataBaseDataSource.getRatesResponse = listOf(GetStandardUnitRatesSampleData.rateEntity[0])

        val result = octopusGraphQLRepository.getStandardUnitRates(
            tariffCode = GetStandardUnitRatesSampleData.rateEntity[0].tariffCode,
            period = Instant.parse("2024-05-07T20:30:00Z")..Instant.parse("2024-05-07T22:00:00Z"),
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetStandardUnitRatesSampleData.rate, actual = result.getOrNull())
    }

    @Test
    fun `getStandardUnitRates should return failure when local database returns an error`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.exception = RuntimeException()

        val result = octopusGraphQLRepository.getStandardUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `getStandardUnitRates should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getRatesResponse = emptyList()

        val result = octopusGraphQLRepository.getStandardUnitRates(
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
                    content = ByteReadChannel(GetStandingChargesSampleData.oeFix12M240628Json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        val result = octopusGraphQLRepository.getStandingCharges(
            tariffCode = "invalid tariff code",
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `getStandingCharges should return cached data when local database contains the required data set`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getRatesResponse = GetStandingChargesSampleData.oeFix12M240628RateEntity

        val result = octopusGraphQLRepository.getStandingCharges(
            tariffCode = GetStandingChargesSampleData.oeFix12M240628RateEntity[0].tariffCode,
            period = GetStandingChargesSampleData.oeFix12M240628RateEntity[0].validFrom..Instant.DISTANT_FUTURE,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetStandingChargesSampleData.oeFix12M240628Rate, actual = result.getOrNull())
    }

    @Test
    fun `getStandingCharges should get data from remote data source when local database contains incomplete data set`() = runTest {
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetStandingChargesSampleData.oeFix12M240628Json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )
        fakeDataBaseDataSource.getRatesResponse = emptyList()

        val result = octopusGraphQLRepository.getStandingCharges(
            tariffCode = GetStandingChargesSampleData.oeFix12M240628RateEntity[0].tariffCode,
            period = GetStandingChargesSampleData.oeFix12M240628RateEntity[0].validFrom..Instant.DISTANT_FUTURE,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetStandingChargesSampleData.oeFix12M240628Rate, actual = result.getOrNull())
    }

    @Test
    fun `getStandingCharges should return failure when local database returns an error`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.exception = RuntimeException()

        val result = octopusGraphQLRepository.getStandingCharges(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `getStandingCharges should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getRatesResponse = emptyList()

        val result = octopusGraphQLRepository.getStandingCharges(
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

        val result = octopusGraphQLRepository.getDayUnitRates(
            tariffCode = "invalid tariff code",
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    // We don't have test data for now
//    @Test
//    fun `getDayUnitRates should return cached data when local database contains the required data set`() = runTest {
//    }
//
//    @Test
//    fun `getDayUnitRates should get data from remote data source when local database contains incomplete data set`() = runTest {
//    }

    @Test
    fun `getDayUnitRates should return failure when local database returns an error`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.exception = RuntimeException()

        val result = octopusGraphQLRepository.getDayUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `getDayUnitRates should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getRatesResponse = emptyList()

        val result = octopusGraphQLRepository.getDayUnitRates(
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

        val result = octopusGraphQLRepository.getNightUnitRates(
            tariffCode = "invalid tariff code",
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    // We don't have test data for now
//    @Test
//    fun `getNightUnitRates should return cached data when local database contains the required data set`() = runTest {
//    }
//
//    @Test
//    fun `getNightUnitRates should get data from remote data source when local database contains incomplete data set`() = runTest {
//    }

    @Test
    fun `getNightUnitRates should return failure when local database returns an error`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.exception = RuntimeException()

        val result = octopusGraphQLRepository.getNightUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `getNightUnitRates should return failure when data source throws an exception`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getRatesResponse = emptyList()

        val result = octopusGraphQLRepository.getNightUnitRates(
            tariffCode = sampleTariffCode,
            period = now..now,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    // 🗂 getConsumption
    @Test
    fun `getConsumption should return cached data when ConsumptionTimeFrame is HALF_HOURLY local database contains the required data set`() = runTest {
        setUpRepository(
            // Fail it if repository needs to reach the backend
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getConsumptionsResponse = listOf(
            ConsumptionEntity(
                deviceId = fakeDeviceId,
                kWhConsumed = 0.113,
                intervalStart = Instant.parse("2024-05-06T23:30:00Z"),
                intervalEnd = Instant.parse("2024-05-07T00:00:00Z"),
                consumptionCost = 0.0,
                standingCharge = 0.0,
            ),
            ConsumptionEntity(
                deviceId = fakeDeviceId,
                kWhConsumed = 0.58,
                intervalStart = Instant.parse("2024-05-06T23:00:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:30:00Z"),
                consumptionCost = 0.0,
                standingCharge = 0.0,
            ),
            ConsumptionEntity(
                deviceId = fakeDeviceId,
                kWhConsumed = 0.201,
                intervalStart = Instant.parse("2024-05-06T22:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:00:00Z"),
                consumptionCost = 0.0,
                standingCharge = 0.0,
            ),
        )

        val result = octopusGraphQLRepository.getConsumption(
            mpan = fakeMpan,
            period = Instant.parse("2024-05-06T22:30:00Z")..Instant.parse("2024-05-06T23:59:59Z"),
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = 3, actual = result.getOrNull()!!.size)
    }

    @Test
    fun `getConsumption should get data from remote data source when ConsumptionTimeFrame is HALF_HOURLY and local database contains incomplete data set`() = runTest {
        mockServer.enqueueString(GetConsumptionSampleData.getMeasurementsQueryResponse)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.getConsumptionsResponse = listOf(
            ConsumptionEntity(
                deviceId = fakeDeviceId,
                kWhConsumed = 0.201,
                intervalStart = Instant.parse("2024-05-06T21:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T22:00:00Z"),
                consumptionCost = 0.0,
                standingCharge = 0.0,
            ),
        )

        val result = octopusGraphQLRepository.getConsumption(
            mpan = fakeMpan,
            period = Instant.parse("2024-05-06T21:30:00Z")..Instant.parse("2024-05-06T23:59:59Z"),
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetConsumptionSampleData.consumptionWithCost, actual = result.getOrNull())
    }

    @Test
    fun `getConsumption should return failure when ConsumptionTimeFrame is HALF_HOURLY and local database returns an error`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.exception = RuntimeException()

        val result = octopusGraphQLRepository.getConsumption(
            mpan = fakeMpan,
            period = Instant.parse("2024-05-06T23:00:00Z")..Instant.parse("2024-05-06T23:59:59Z"),
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `getConsumption should get data from remote data source without checking the cache when ConsumptionTimeFrame is not HALF_HOURLY`() = runTest {
        setUpRepository(
            engine = mockEngineInternalServerError,
        )
        fakeDataBaseDataSource.exception = RuntimeException()

        listOf(ConsumptionTimeFrame.DAY, ConsumptionTimeFrame.WEEK, ConsumptionTimeFrame.MONTH, ConsumptionTimeFrame.QUARTER).forEach { timeFrame ->
            mockServer.enqueueString(GetConsumptionSampleData.getMeasurementsQueryResponse)
            val result = octopusGraphQLRepository.getConsumption(
                mpan = fakeMpan,
                period = Instant.parse("2024-05-06T21:30:00Z")..Instant.parse("2024-05-06T23:59:59Z"),
                groupBy = timeFrame,
                accountNumber = fakeAccountNumber,
                deviceId = fakeDeviceId,
            )

            assertTrue(result.isSuccess)
            assertEquals(expected = GetConsumptionSampleData.consumptionWithCost, actual = result.getOrNull())
        }
    }

    @Test
    fun `getConsumption should return failure when remote data source throws an exception`() = runTest {
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        fakeDataBaseDataSource.getConsumptionsResponse = emptyList()
        val result = octopusGraphQLRepository.getConsumption(
            mpan = fakeMpan,
            period = now..now.plus(Duration.parse("1d")),
            groupBy = ConsumptionTimeFrame.HALF_HOURLY,
            accountNumber = fakeAccountNumber,
            deviceId = fakeDeviceId,
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApolloHttpException)
    }

    // 🗂 getAccount
    @Test
    fun `getAccount should return expected domain model`() = runTest {
        mockServer.enqueueString(GetAccountSampleData.propertiesQueryResponse)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val result = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isSuccess)
        assertEquals(expected = GetAccountSampleData.account, actual = result.getOrNull())
    }

    @Test
    fun `getAccount should return Success-null when data source gives null response`() = runTest {
        mockServer.enqueueString(GetAccountSampleData.emptyPropertiesQueryResponse)
        setUpRepository(engine = mockEngineNotFound)

        val result = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isSuccess)
        assertNull(actual = result.getOrNull())
    }

    @Test
    fun `getAccount should return Failure when data source throws an exception`() = runTest {
        setUpRepository(engine = mockEngineInternalServerError)
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)

        val result = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )

        assertTrue(result.isFailure)
        assertTrue(actual = result.exceptionOrNull() is ApolloHttpException)
    }

    @Test
    fun `getAccount should return cached result when it hits the cache`() = runTest {
        mockServer.enqueueString(GetAccountSampleData.propertiesQueryResponse)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val firstAccess = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )
        assertTrue(firstAccess.isSuccess)
        assertEquals(expected = GetAccountSampleData.account, actual = firstAccess.getOrNull())

        mockServer.enqueue(mockResponse = mockResponseInternalServerError)
        val secondAccess = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )
        assertTrue(secondAccess.isSuccess)
        assertEquals(expected = GetAccountSampleData.account, actual = secondAccess.getOrNull())
    }

    @Test
    fun `clearCache should clear cached account`() = runTest {
        mockServer.enqueueString(GetAccountSampleData.propertiesQueryResponse)
        setUpRepository(
            engine = mockEngineInternalServerError,
        )

        val firstAccess = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )

        firstAccess.exceptionOrNull()?.printStackTrace()
        assertTrue(firstAccess.isSuccess)
        assertEquals(expected = GetAccountSampleData.account, actual = firstAccess.getOrNull())

        octopusGraphQLRepository.clearCache()

        // Repository reach endpoint because of cache missed, and we set endpoint to return error
        mockServer.enqueue(mockResponse = mockResponseInternalServerError)
        val secondAccess = octopusGraphQLRepository.getAccount(
            accountNumber = fakeAccountNumber,
        )
        assertTrue(secondAccess.isFailure)
        assertTrue(actual = secondAccess.exceptionOrNull() is ApolloHttpException)
    }

    @Test
    fun `clearCache should clear local database`() = runTest {
        val errorMessage = "exception to test method triggered"
        setUpRepository(
            engine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(GetAccountSampleData.json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )
        fakeDataBaseDataSource.exception = IOException(errorMessage)

        val exception = assertFailsWith<IOException> {
            octopusGraphQLRepository.clearCache()
        }
        assertEquals(expected = errorMessage, actual = exception.message)
    }
}
