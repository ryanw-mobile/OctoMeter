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

package com.rwmobi.kunigami.domain.usecase.account

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.test.samples.AccountSampleData
import com.rwmobi.kunigami.test.samples.TariffSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SyncUserProfileUseCaseTest {
    private lateinit var syncUserProfileUseCase: SyncUserProfileUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository

    // common values not affecting the test results
    private val fakeApiKey = "test_api_key"
    private val fakeAccountNumber = "A-9009A9A9"
    private val fakeMpan = "9900000999999"
    private val fakeMeterSerialNumber = "99A9999999"
    private val fakeAccount = AccountSampleData.accountA1234A1B1

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeOctopusApiRepository()
        syncUserProfileUseCase = SyncUserProfileUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `should return IncompleteCredentialsException when in demo mode`() = runTest {
        fakeUserPreferenceRepository.demoMode = true

        val result = syncUserProfileUseCase()

        assertTrue(result.isFailure)
        assertIs<IncompleteCredentialsException>(result.exceptionOrNull())
    }

    @Test
    fun `should return UserProfile when all data is present`() = runTest {
        fakeUserPreferenceRepository.apply {
            demoMode = false
            apiKey = fakeApiKey
            accountNumber = fakeAccount.accountNumber
            mpan = fakeAccount.electricityMeterPoints.first().mpan
            meterSerialNumber = fakeAccount.electricityMeterPoints.first().meters.first().serialNumber
        }
        fakeRestApiRepository.setSimpleProductTariffResponse = Result.success(TariffSampleData.var221101)
        fakeRestApiRepository.setAccountResponse = Result.success(fakeAccount)

        val result = syncUserProfileUseCase()

        assertTrue(result.isSuccess)
        val userProfile = result.getOrNull()
        assertIs<UserProfile>(userProfile)
        assertEquals(fakeAccount.electricityMeterPoints.first().mpan, userProfile.selectedMpan)
        assertEquals(fakeAccount.electricityMeterPoints.first().meters.first().serialNumber, userProfile.selectedMeterSerialNumber)
        assertEquals(fakeAccount, userProfile.account)
    }

    @Test
    fun `should return null when account is null`() = runTest {
        fakeUserPreferenceRepository.apply {
            demoMode = false
            apiKey = fakeApiKey
            accountNumber = fakeAccountNumber
        }
        fakeRestApiRepository.setAccountResponse = Result.success(null)

        val result = syncUserProfileUseCase()

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `should return null when tariff summary is null`() = runTest {
        fakeUserPreferenceRepository.apply {
            demoMode = false
            apiKey = fakeApiKey
            accountNumber = fakeAccountNumber
            mpan = fakeMpan
            meterSerialNumber = fakeMeterSerialNumber
        }
        fakeRestApiRepository.setAccountResponse = Result.success(fakeAccount)
        fakeRestApiRepository.setSimpleProductTariffResponse = null

        val result = syncUserProfileUseCase()

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }
}
