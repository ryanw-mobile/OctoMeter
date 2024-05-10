/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.data.source.network

import com.rwmobi.kunigame.data.source.network.samples.GetConsumptionSampleData
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

class ElectricityMeterPointsEndpointTest {
    private val fakeBaseUrl = "https://some.fakeurl.com"
    private val fakeApiKey = "sk_live_xXxX1xx1xx1xx1XXxX1Xxx1x"
    private val fakeMpan = "1100000111111"
    private val fakeMeterSerialNumber = "11L1111111"

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

    // 🗂 getConsumption
    @Test
    fun getConsumption_ShouldReturnExpectedDto_WhenHttpStatusIsOK() = runTest {
        val electricityMeterPointsEndpoint = ElectricityMeterPointsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.OK,
                contentType = "application/json",
                payload = GetConsumptionSampleData.json,
            ),
        )

        val result = electricityMeterPointsEndpoint.getConsumption(
            apiKey = fakeApiKey,
            mpan = fakeMpan,
            meterSerialNumber = fakeMeterSerialNumber,
        )
        result shouldBe GetConsumptionSampleData.dto
    }

    @Test
    fun getConsumption_ShouldThrowNoTransformationFoundException_WhenHttpStatusIsInternalServerError() = runTest {
        val electricityMeterPointsEndpoint = ElectricityMeterPointsEndpoint(
            baseUrl = fakeBaseUrl,
            httpClient = setupEngine(
                status = HttpStatusCode.InternalServerError,
                contentType = "text/html",
                payload = "Internal Server Error",
            ),
        )

        shouldThrowExactly<NoTransformationFoundException> {
            electricityMeterPointsEndpoint.getConsumption(
                apiKey = fakeApiKey,
                mpan = fakeMpan,
                meterSerialNumber = fakeMeterSerialNumber,
            )
        }
    }
}
