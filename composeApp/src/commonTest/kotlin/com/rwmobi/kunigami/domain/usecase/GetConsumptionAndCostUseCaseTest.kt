/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup
import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetConsumptionAndCostUseCaseTest {
    private val fakePeriodFrom = Instant.DISTANT_PAST
    private val fakePeriodTo = Instant.DISTANT_FUTURE
    private val groupBy = ConsumptionDataGroup.HALF_HOURLY

    private lateinit var getConsumptionAndCostUseCase: GetConsumptionAndCostUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeRestApiRepository
    private lateinit var fakeDemoRestApiRepository: FakeRestApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeRestApiRepository()
        fakeDemoRestApiRepository = FakeRestApiRepository()
        getConsumptionAndCostUseCase = GetConsumptionAndCostUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            restApiRepository = fakeRestApiRepository,
            demoRestApiRepository = fakeDemoRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return consumption successfully when valid data is provided`() = runTest {
        val apiKey = "test_api_key"
        val mpan = "mpan_123"
        val meterSerialNumber = "meter_123"

        val expectedConsumption = listOf(
            Consumption(
                kWhConsumed = 0.113,
                intervalStart = Instant.parse("2024-05-06T23:30:00Z"),
                intervalEnd = Instant.parse("2024-05-07T00:00:00Z"),
            ),
            Consumption(
                kWhConsumed = 0.58,
                intervalStart = Instant.parse("2024-05-06T23:00:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:30:00Z"),
            ),
            Consumption(
                kWhConsumed = 0.201,
                intervalStart = Instant.parse("2024-05-06T22:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:00:00Z"),
            ),
        )

        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber
        fakeRestApiRepository.setConsumptionResponse = Result.success(expectedConsumption)

        val result = getConsumptionAndCostUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        assertTrue(result.isSuccess)
        assertEquals(expectedConsumption.sortedBy { it.intervalStart }, result.getOrNull())
    }

    @Test
    fun `invoke should throw exception when API key is missing`() = runTest {
        val apiKey = null
        val mpan = "mpan_123"
        val meterSerialNumber = "meter_123"

        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber

        val exception = assertFailsWith<IllegalArgumentException> {
            getConsumptionAndCostUseCase(
                periodFrom = fakePeriodFrom,
                periodTo = fakePeriodTo,
                groupBy = groupBy,
            ).getOrThrow()
        }

        assertEquals("API Key is null", exception.message)
    }

    @Test
    fun `invoke should throw exception when MPAN is missing`() = runTest {
        val apiKey = "test_api_key"
        val mpan = null
        val meterSerialNumber = "meter_123"

        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber

        val exception = assertFailsWith<IllegalArgumentException> {
            getConsumptionAndCostUseCase(
                periodFrom = fakePeriodFrom,
                periodTo = fakePeriodTo,
                groupBy = groupBy,
            ).getOrThrow()
        }

        assertEquals("MPAN is null", exception.message)
    }

    @Test
    fun `invoke should throw exception when meter serial number is missing`() = runTest {
        val apiKey = "test_api_key"
        val mpan = "mpan_123"
        val meterSerialNumber = null

        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber

        val exception = assertFailsWith<IllegalArgumentException> {
            getConsumptionAndCostUseCase(
                periodFrom = fakePeriodFrom,
                periodTo = fakePeriodTo,
                groupBy = groupBy,
            ).getOrThrow()
        }

        assertEquals("Meter Serial Number is null", exception.message)
    }

    @Test
    fun `invoke should throw exception when API call fails`() = runTest {
        val apiKey = "test_api_key"
        val mpan = "mpan_123"
        val meterSerialNumber = "meter_123"
        val errorMessage = "API Error"

        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber
        fakeRestApiRepository.setConsumptionResponse = Result.failure(RuntimeException(errorMessage))

        val exception = assertFailsWith<RuntimeException> {
            getConsumptionAndCostUseCase(
                periodFrom = fakePeriodFrom,
                periodTo = fakePeriodTo,
                groupBy = groupBy,
            ).getOrThrow()
        }

        assertEquals(errorMessage, exception.message)
    }

    // Demo mode
    @Test
    fun `invoke should return consumption from fakeDemoRestApiRepository when under demoMode`() = runTest {
        val expectedConsumption = listOf(
            Consumption(
                kWhConsumed = 0.113,
                intervalStart = Instant.parse("2024-05-06T23:30:00Z"),
                intervalEnd = Instant.parse("2024-05-07T00:00:00Z"),
            ),
            Consumption(
                kWhConsumed = 0.58,
                intervalStart = Instant.parse("2024-05-06T23:00:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:30:00Z"),
            ),
            Consumption(
                kWhConsumed = 0.201,
                intervalStart = Instant.parse("2024-05-06T22:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:00:00Z"),
            ),
        )

        fakeUserPreferenceRepository.demoMode = true
        fakeRestApiRepository.setConsumptionResponse = Result.failure(RuntimeException("getConsumptionUseCase should call DemoRestApiRepository under demo mode"))
        fakeDemoRestApiRepository.setConsumptionResponse = Result.success(expectedConsumption)

        val result = getConsumptionAndCostUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        assertTrue(result.isSuccess)
        assertEquals(expectedConsumption.sortedBy { it.intervalStart }, result.getOrNull())
    }
}
