/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.samples.AccountSampleData
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
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeRestApiRepository()
        syncUserProfileUseCase = SyncUserProfileUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `should return IncompleteCredentialsException when in demo mode`() = runTest {
        fakeUserPreferenceRepository.demoMode = true

        val result = syncUserProfileUseCase.invoke()

        assertTrue(result.isFailure)
        assertIs<IncompleteCredentialsException>(result.exceptionOrNull())
    }

    @Test
    fun `should return UserProfile when all data is present`() = runTest {
        val apiKey = "validApiKey"
        val account = AccountSampleData.accountA1234A1B1
        fakeUserPreferenceRepository.apply {
            demoMode = false
            this.apiKey = apiKey
            this.accountNumber = account.accountNumber
            this.mpan = account.electricityMeterPoints.first().mpan
            this.meterSerialNumber = account.electricityMeterPoints.first().meterSerialNumbers.first()
        }
        fakeRestApiRepository.setAccountResponse = Result.success(listOf(account))

        val result = syncUserProfileUseCase()

        assertTrue(result.isSuccess)
        val userProfile = result.getOrNull()
        assertIs<UserProfile>(userProfile)
        assertEquals(account.electricityMeterPoints.first().mpan, userProfile!!.selectedMpan)
        assertEquals(account.electricityMeterPoints.first().meterSerialNumbers.first(), userProfile.selectedMeterSerialNumber)
        assertEquals(account, userProfile.account)
    }

    @Test
    fun `should return null when account is null`() = runTest {
        val apiKey = "validApiKey"
        val accountNumber = "validAccountNumber"
        fakeUserPreferenceRepository.apply {
            demoMode = false
            this.apiKey = apiKey
            this.accountNumber = accountNumber
        }
        fakeRestApiRepository.setAccountResponse = Result.success(emptyList())

        val result = syncUserProfileUseCase()

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `should return null when tariff summary is null`() = runTest {
        val apiKey = "validApiKey"
        val accountNumber = "validAccountNumber"
        val mpan = "validMpan"
        val meterSerialNumber = "validMeterSerialNumber"
        fakeUserPreferenceRepository.apply {
            demoMode = false
            this.apiKey = apiKey
            this.accountNumber = accountNumber
            this.mpan = mpan
            this.meterSerialNumber = meterSerialNumber
        }
        val account = AccountSampleData.accountA1234A1B1
        fakeRestApiRepository.setAccountResponse = Result.success(listOf(account))
        fakeRestApiRepository.setSimpleProductTariffResponse = null

        val result = syncUserProfileUseCase()

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }
}
