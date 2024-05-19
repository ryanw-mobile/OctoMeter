/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
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

class GetUserAccountUseCaseTest {

    private lateinit var getUserAccountUseCase: GetUserAccountUseCase
    private lateinit var fakeUserPreferenceRepository: FakeUserPreferencesRepository
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    private val fakeAccountNumber = "A1234A1B1"
    private val fakeApiKey = "test_api_key"

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferenceRepository = FakeUserPreferencesRepository()
        fakeRestApiRepository = FakeRestApiRepository()
        getUserAccountUseCase = GetUserAccountUseCase(
            userPreferencesRepository = fakeUserPreferenceRepository,
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return account successfully when credentials are provided`() = runTest {
        val expectedAccount = AccountSampleData.accountA1234A1B1
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = fakeApiKey
        fakeUserPreferenceRepository.accountNumber = fakeAccountNumber
        fakeRestApiRepository.setAccountResponse = Result.success(listOf(expectedAccount))

        val result = getUserAccountUseCase()

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe expectedAccount
    }

    @Test
    fun `invoke should throw IncompleteCredentialsException when in demo mode`() = runTest {
        fakeUserPreferenceRepository.demoMode = true

        val result = getUserAccountUseCase()

        result.isFailure shouldBe true
        result.exceptionOrNull() should beInstanceOf<IncompleteCredentialsException>()
    }

    @Test
    fun `invoke should throw exception when API key is missing`() = runTest {
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = null
        fakeUserPreferenceRepository.accountNumber = fakeAccountNumber

        val result = getUserAccountUseCase()

        result.isFailure shouldBe true
        result.exceptionOrNull() should beInstanceOf<IllegalStateException>()
        result.exceptionOrNull()!!.message shouldBe "Expect API Key but null"
    }

    @Test
    fun `invoke should throw exception when account number is missing`() = runTest {
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = fakeApiKey
        fakeUserPreferenceRepository.accountNumber = null

        val result = getUserAccountUseCase()

        result.isFailure shouldBe true
        result.exceptionOrNull() should beInstanceOf<IllegalStateException>()
        result.exceptionOrNull()!!.message shouldBe "Expect Account Number but null"
    }

    @Test
    fun `invoke should throw exception when API call fails`() = runTest {
        fakeUserPreferenceRepository.demoMode = false
        fakeUserPreferenceRepository.apiKey = fakeApiKey
        fakeUserPreferenceRepository.accountNumber = fakeAccountNumber
        val errorMessage = "API Error"
        fakeRestApiRepository.setAccountResponse = Result.failure(RuntimeException(errorMessage))

        val result = getUserAccountUseCase()

        result.isFailure shouldBe true
        result.exceptionOrNull() should beInstanceOf<RuntimeException>()
        result.exceptionOrNull()!!.message shouldBe errorMessage
    }
}
