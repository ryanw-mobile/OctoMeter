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

package com.rwmobi.kunigami.domain.usecase.consumption

import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.test.samples.AccountSampleData
import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class GetConsumptionAndCostUseCaseTest {
    private val fakePeriodFrom = Instant.DISTANT_PAST
    private val fakePeriodTo = Instant.DISTANT_FUTURE
    private val groupBy = ConsumptionTimeFrame.HALF_HOURLY

    private lateinit var getConsumptionAndCostUseCase: GetConsumptionAndCostUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository
    private lateinit var fakeDemoRestApiRepository: FakeOctopusApiRepository

    // common values not affecting the test results
    private val fakeApiKey = "test_api_key"
    private val fakeAccountNumber = "A-9009A9A9"
    private val fakeMpan = "9900000999999"
    private val fakeMeterSerialNumber = "99A9999999"
    private val fakeAccount = AccountSampleData.accountA1234A1B1
    private val sampleUserProfile = UserProfile(
        selectedMpan = AccountSamples.account928.electricityMeterPoints[0].mpan,
        selectedMeterSerialNumber = AccountSamples.account928.electricityMeterPoints[0].meters[0].serialNumber,
        account = AccountSamples.account928,
    )

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeOctopusApiRepository()
        fakeDemoRestApiRepository = FakeOctopusApiRepository()
        getConsumptionAndCostUseCase = GetConsumptionAndCostUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            octopusApiRepository = fakeRestApiRepository,
            demoOctopusApiRepository = fakeDemoRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return consumption successfully when valid data is provided`() = runTest {
        val expectedConsumption = listOf(
            ConsumptionWithCost(
                consumption = Consumption(
                    kWhConsumed = 0.113,
                    interval = Instant.parse("2024-05-06T23:30:00Z")..Instant.parse("2024-05-07T00:00:00Z"),
                ),
                vatInclusiveCost = 0.0,
                vatInclusiveStandingCharge = 0.0,
            ),
            ConsumptionWithCost(
                consumption = Consumption(
                    kWhConsumed = 0.58,
                    interval = Instant.parse("2024-05-06T23:00:00Z")..Instant.parse("2024-05-06T23:30:00Z"),
                ),
                vatInclusiveCost = 0.0,
                vatInclusiveStandingCharge = 0.0,
            ),
            ConsumptionWithCost(
                consumption = Consumption(
                    kWhConsumed = 0.201,
                    interval = Instant.parse("2024-05-06T22:30:00Z")..Instant.parse("2024-05-06T23:00:00Z"),
                ),
                vatInclusiveCost = 0.0,
                vatInclusiveStandingCharge = 0.0,
            ),
        )

        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = fakeApiKey
        fakeUserPreferenceRepository.mpan = fakeMpan
        fakeUserPreferenceRepository.accountNumber = fakeAccountNumber
        fakeUserPreferenceRepository.meterSerialNumber = fakeMeterSerialNumber
        fakeRestApiRepository.setAccountResponse = Result.success(fakeAccount)
        fakeRestApiRepository.setConsumptionResponse = Result.success(expectedConsumption)

        val result = getConsumptionAndCostUseCase(
            period = fakePeriodFrom..fakePeriodTo,
            groupBy = groupBy,
            userProfile = sampleUserProfile,
        )

        assertTrue(result.isSuccess)
        assertEquals(expectedConsumption.sortedBy { it.consumption.interval.start }, result.getOrNull())
    }

    @Test
    fun `invoke should throw exception when API key is missing`() = runTest {
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = null
        fakeUserPreferenceRepository.mpan = fakeMpan
        fakeUserPreferenceRepository.meterSerialNumber = fakeMeterSerialNumber

        val exception = assertFailsWith<IllegalArgumentException> {
            getConsumptionAndCostUseCase(
                period = fakePeriodFrom..fakePeriodTo,
                groupBy = groupBy,
                userProfile = sampleUserProfile,
            ).getOrThrow()
        }

        assertEquals("Assertion failed: API Key is null", exception.message)
    }

    @Test
    fun `invoke should throw exception when device Id is missing`() = runTest {
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = fakeApiKey
        fakeUserPreferenceRepository.mpan = fakeMpan
        fakeUserPreferenceRepository.meterSerialNumber = null
        val userProfileWithNonExistentSelectedMeterSerialNumber = sampleUserProfile.copy(
            selectedMeterSerialNumber = "invalid-serial-number",
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            getConsumptionAndCostUseCase(
                period = fakePeriodFrom..fakePeriodTo,
                groupBy = groupBy,
                userProfile = userProfileWithNonExistentSelectedMeterSerialNumber,
            ).getOrThrow()
        }

        assertEquals("Assertion failed: deviceId is null", exception.message)
    }

    @Test
    fun `invoke should throw exception when API call fails`() = runTest {
        val errorMessage = "API Error"
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = fakeApiKey
        fakeUserPreferenceRepository.mpan = fakeMpan
        fakeUserPreferenceRepository.accountNumber = fakeAccountNumber
        fakeUserPreferenceRepository.meterSerialNumber = fakeMeterSerialNumber
        fakeRestApiRepository.setAccountResponse = Result.success(fakeAccount)
        fakeRestApiRepository.setConsumptionResponse = Result.failure(RuntimeException(errorMessage))

        val exception = assertFailsWith<RuntimeException> {
            getConsumptionAndCostUseCase(
                period = fakePeriodFrom..fakePeriodTo,
                groupBy = groupBy,
                userProfile = sampleUserProfile,
            ).getOrThrow()
        }

        assertEquals(errorMessage, exception.message)
    }

    // Demo mode
    @Test
    fun `invoke should return consumption from fakeDemoRestApiRepository when under demoMode`() = runTest {
        val expectedConsumption = listOf(
            ConsumptionWithCost(
                consumption = Consumption(
                    kWhConsumed = 0.113,
                    interval = Instant.parse("2024-05-06T23:30:00Z")..Instant.parse("2024-05-07T00:00:00Z"),
                ),
                vatInclusiveCost = 1.0,
                vatInclusiveStandingCharge = 0.054,
            ),
            ConsumptionWithCost(
                consumption = Consumption(
                    kWhConsumed = 0.58,
                    interval = Instant.parse("2024-05-06T23:00:00Z")..Instant.parse("2024-05-06T23:30:00Z"),
                ),
                vatInclusiveCost = 2.0,
                vatInclusiveStandingCharge = 0.054,
            ),
            ConsumptionWithCost(
                consumption = Consumption(
                    kWhConsumed = 0.201,
                    interval = Instant.parse("2024-05-06T22:30:00Z")..Instant.parse("2024-05-06T23:00:00Z"),
                ),
                vatInclusiveCost = 3.0,
                vatInclusiveStandingCharge = 0.054,
            ),
        )

        fakeUserPreferenceRepository.demoMode = true
        fakeRestApiRepository.setStandardUnitRatesResponse = Result.success(emptyList())
        fakeRestApiRepository.setConsumptionResponse = Result.failure(RuntimeException("getConsumptionUseCase should call DemoRestApiRepository under demo mode"))
        fakeDemoRestApiRepository.setConsumptionResponse = Result.success(expectedConsumption)

        val result = getConsumptionAndCostUseCase(
            period = fakePeriodFrom..fakePeriodTo,
            groupBy = groupBy,
            userProfile = sampleUserProfile,
        )

        assertTrue(result.isSuccess)
        assertEquals(expectedConsumption.sortedBy { it.consumption.interval.start }, result.getOrNull())
    }
}
