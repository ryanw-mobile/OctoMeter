/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase.account

import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ClearCacheUseCaseTest {
    private lateinit var clearCacheUseCase: ClearCacheUseCase
    private lateinit var fakeRestApiRepository: FakeOctopusApiRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setupUseCase() {
        fakeRestApiRepository = FakeOctopusApiRepository()
        clearCacheUseCase = ClearCacheUseCase(
            octopusApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `clearCacheUseCase should return success if repository did return error`() = runTest {
        val result = clearCacheUseCase()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `clearCacheUseCase should return success if repository threw an exception`() = runTest {
        fakeRestApiRepository.setClearCacheException = IOException("sample error message")
        val result = clearCacheUseCase()
        assertTrue(result.isFailure)
        assertTrue(actual = result.exceptionOrNull() is IOException)
    }
}
