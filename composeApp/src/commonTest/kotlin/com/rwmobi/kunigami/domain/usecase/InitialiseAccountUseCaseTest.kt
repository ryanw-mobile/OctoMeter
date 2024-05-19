/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.NoValidMeterException
import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.samples.AccountSampleData
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InitialiseAccountUseCaseTest {

    private lateinit var initialiseAccountUseCase: InitialiseAccountUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    private val fakeAccountNumber = "A1234A1B1"
    private val fakeApiKey = "test_api_key"

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeRestApiRepository()
        initialiseAccountUseCase = InitialiseAccountUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should initialize account successfully when valid data is provided`() = runTest {
        val apiKey = fakeApiKey
        val accountNumber = fakeAccountNumber
        val expectedAccount = AccountSampleData.accountA1234A1B1
        fakeRestApiRepository.setAccountResponse = Result.success(listOf(expectedAccount))

        val result = initialiseAccountUseCase(apiKey, accountNumber)

        result.isSuccess shouldBe true
        fakeUserPreferenceRepository.apiKey shouldBe apiKey
        fakeUserPreferenceRepository.accountNumber shouldBe accountNumber
        fakeUserPreferenceRepository.mpan shouldBe AccountSampleData.accountA1234A1B1.electricityMeterPoints[0].mpan
        fakeUserPreferenceRepository.meterSerialNumber shouldBe AccountSampleData.accountA1234A1B1.electricityMeterPoints[0].meterSerialNumbers[0]
    }

    @Test
    fun `invoke should throw NoValidMeterException when account has no valid meters`() = runTest {
        val apiKey = fakeApiKey
        val accountNumber = fakeAccountNumber
        fakeRestApiRepository.setAccountResponse = Result.success(emptyList())

        val result = initialiseAccountUseCase(apiKey, accountNumber)

        result.isFailure shouldBe true
        result.exceptionOrNull() should beInstanceOf<NoValidMeterException>()
    }

    @Test
    fun `invoke should throw exception when API call fails`() = runTest {
        val apiKey = fakeApiKey
        val accountNumber = fakeAccountNumber
        val errorMessage = "API Error"
        fakeRestApiRepository.setAccountResponse = Result.failure(RuntimeException(errorMessage))

        val result = initialiseAccountUseCase(apiKey, accountNumber)

        result.isFailure shouldBe true
        result.exceptionOrNull() should beInstanceOf<RuntimeException>()
        result.exceptionOrNull()!!.message shouldBe errorMessage
    }
}
