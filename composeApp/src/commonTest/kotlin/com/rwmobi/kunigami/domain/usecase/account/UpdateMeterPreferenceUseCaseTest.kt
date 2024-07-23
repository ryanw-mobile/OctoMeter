/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase.account

import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMeterPreferenceUseCaseTest {

    private lateinit var updateMeterPreferenceUseCase: UpdateMeterPreferenceUseCase
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository

    @BeforeTest
    fun setupUseCase() {
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        updateMeterPreferenceUseCase = UpdateMeterPreferenceUseCase(
            userPreferencesRepository = fakeUserPreferencesRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should update meter preferences successfully`() = runTest {
        val mpan = "mpan_123"
        val meterSerialNumber = "meter_123"

        val result = updateMeterPreferenceUseCase(mpan, meterSerialNumber)

        assertTrue(result.isSuccess)
        assertEquals(mpan, fakeUserPreferencesRepository.mpan)
        assertEquals(meterSerialNumber, fakeUserPreferencesRepository.meterSerialNumber)
    }
}
