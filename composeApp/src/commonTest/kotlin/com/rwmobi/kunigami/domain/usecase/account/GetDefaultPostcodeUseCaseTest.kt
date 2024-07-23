/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase.account

import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.test.samples.AccountSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetDefaultPostcodeUseCaseTest {
    private lateinit var getDefaultPostcodeUseCase: GetDefaultPostcodeUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository

    // Common values not affecting the test results
    private val fakeApiKey = "test_api_key"
    private val fakeAccountNumber = "A-9009A9A9"
    private val defaultPostcode = "WC1X 0ND"
    private val fakeAccount = AccountSampleData.accountB1234A1A1

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeOctopusApiRepository()
        getDefaultPostcodeUseCase = GetDefaultPostcodeUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `should return default postcode when in demo mode`() = runTest {
        fakeUserPreferenceRepository.demoMode = true

        val result = getDefaultPostcodeUseCase()

        assertEquals(defaultPostcode, result)
    }

    @Test
    fun `should return postcode from account when not in demo mode`() = runTest {
        fakeUserPreferenceRepository.apply {
            demoMode = false
            apiKey = fakeApiKey
            accountNumber = fakeAccount.accountNumber
        }
        fakeRestApiRepository.setAccountResponse = Result.success(fakeAccount)

        val result = getDefaultPostcodeUseCase()

        assertEquals(fakeAccount.postcode, result)
    }

    @Test
    fun `should return default postcode when account fetch fails`() = runTest {
        fakeUserPreferenceRepository.apply {
            demoMode = false
            apiKey = fakeApiKey
            accountNumber = fakeAccountNumber
        }
        fakeRestApiRepository.setAccountResponse = Result.failure(Exception("Network error"))

        val result = getDefaultPostcodeUseCase()

        assertEquals(defaultPostcode, result)
    }
}
