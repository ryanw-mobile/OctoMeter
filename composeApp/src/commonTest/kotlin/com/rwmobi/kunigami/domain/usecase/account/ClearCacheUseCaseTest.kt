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

import com.rwmobi.kunigami.domain.repository.FakeOctopusApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
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
