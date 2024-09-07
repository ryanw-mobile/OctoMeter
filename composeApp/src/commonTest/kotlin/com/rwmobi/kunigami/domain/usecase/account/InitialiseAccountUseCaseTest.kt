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

import com.rwmobi.kunigami.domain.exceptions.NoValidMeterException
import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.test.samples.AccountSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class InitialiseAccountUseCaseTest {

    private lateinit var initialiseAccountUseCase: InitialiseAccountUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository

    private val fakeAccountNumber = "A1234A1B1"
    private val fakeApiKey = "test_api_key"

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeOctopusApiRepository()
        initialiseAccountUseCase = InitialiseAccountUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should initialize account successfully when valid data is provided`() = runTest {
        val apiKey = fakeApiKey
        val accountNumber = fakeAccountNumber
        val expectedAccount = AccountSampleData.accountA1234A1B1
        fakeRestApiRepository.setAccountResponse = Result.success(expectedAccount)

        val result = initialiseAccountUseCase(apiKey, accountNumber)

        assertTrue(result.isSuccess)
        assertEquals(apiKey, fakeUserPreferenceRepository.apiKey)
        assertEquals(accountNumber, fakeUserPreferenceRepository.accountNumber)
        assertEquals(AccountSampleData.accountA1234A1B1.electricityMeterPoints[0].mpan, fakeUserPreferenceRepository.mpan)
        assertEquals(AccountSampleData.accountA1234A1B1.electricityMeterPoints[0].meters[0].serialNumber, fakeUserPreferenceRepository.meterSerialNumber)
    }

    @Test
    fun `invoke should throw NoValidMeterException when account has no valid meters`() = runTest {
        val apiKey = fakeApiKey
        val accountNumber = fakeAccountNumber
        fakeRestApiRepository.setAccountResponse = Result.success(null)

        val result = initialiseAccountUseCase(apiKey, accountNumber)

        assertTrue(result.isFailure)
        assertIs<NoValidMeterException>(result.exceptionOrNull())
    }

    @Test
    fun `invoke should throw exception when API call fails`() = runTest {
        val apiKey = fakeApiKey
        val accountNumber = fakeAccountNumber
        val errorMessage = "API Error"
        fakeRestApiRepository.setAccountResponse = Result.failure(RuntimeException(errorMessage))

        val exception = assertFailsWith<RuntimeException> {
            initialiseAccountUseCase(apiKey, accountNumber).getOrThrow()
        }

        assertEquals(errorMessage, exception.message)
    }
}
