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
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetConsumptionUseCaseTest {
    private val fakePeriodFrom = Instant.DISTANT_PAST
    private val fakePeriodTo = Instant.DISTANT_FUTURE
    private val groupBy = ConsumptionDataGroup.HALF_HOURLY

    private lateinit var getConsumptionUseCase: GetConsumptionUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository().apply {
            demoMode = false // We do not test demo mode
        }
        fakeRestApiRepository = FakeRestApiRepository()
        getConsumptionUseCase = GetConsumptionUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            restApiRepository = fakeRestApiRepository,
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
                consumption = 0.113,
                intervalStart = Instant.parse("2024-05-06T23:30:00Z"),
                intervalEnd = Instant.parse("2024-05-07T00:00:00Z"),
            ),
            Consumption(
                consumption = 0.58,
                intervalStart = Instant.parse("2024-05-06T23:00:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:30:00Z"),
            ),
            Consumption(
                consumption = 0.201,
                intervalStart = Instant.parse("2024-05-06T22:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:00:00Z"),
            ),
        )

        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber
        fakeRestApiRepository.setConsumptionResponse = Result.success(expectedConsumption)

        val result = getConsumptionUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe expectedConsumption.sortedBy { it.intervalStart }
    }

    @Test
    fun `invoke should throw exception when API key is missing`() = runTest {
        val apiKey = null
        val mpan = "mpan_123"
        val meterSerialNumber = "meter_123"

        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber

        val result = getConsumptionUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        result.isFailure shouldBe true
        result.exceptionOrNull().shouldBeInstanceOf<IllegalArgumentException>()
        result.exceptionOrNull()!!.message shouldBe "API Key is null"
    }

    @Test
    fun `invoke should throw exception when MPAN is missing`() = runTest {
        val apiKey = "test_api_key"
        val mpan = null
        val meterSerialNumber = "meter_123"

        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber

        val result = getConsumptionUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        result.isFailure shouldBe true
        result.exceptionOrNull().shouldBeInstanceOf<IllegalArgumentException>()
        result.exceptionOrNull()!!.message shouldBe "MPAN is null"
    }

    @Test
    fun `invoke should throw exception when meter serial number is missing`() = runTest {
        val apiKey = "test_api_key"
        val mpan = "mpan_123"
        val meterSerialNumber = null

        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber

        val result = getConsumptionUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        result.isFailure shouldBe true
        result.exceptionOrNull().shouldBeInstanceOf<IllegalArgumentException>()
        result.exceptionOrNull()!!.message shouldBe "Meter Serial Number is null"
    }

    @Test
    fun `invoke should throw exception when API call fails`() = runTest {
        val apiKey = "test_api_key"
        val mpan = "mpan_123"
        val meterSerialNumber = "meter_123"
        val errorMessage = "API Error"

        fakeUserPreferenceRepository.apiKey = apiKey
        fakeUserPreferenceRepository.mpan = mpan
        fakeUserPreferenceRepository.meterSerialNumber = meterSerialNumber
        fakeRestApiRepository.setConsumptionResponse = Result.failure(RuntimeException(errorMessage))

        val result = getConsumptionUseCase(
            periodFrom = fakePeriodFrom,
            periodTo = fakePeriodTo,
            groupBy = groupBy,
        )

        result.isFailure shouldBe true
        result.exceptionOrNull().shouldBeInstanceOf<RuntimeException>()
        result.exceptionOrNull()!!.message shouldBe errorMessage
    }
}
