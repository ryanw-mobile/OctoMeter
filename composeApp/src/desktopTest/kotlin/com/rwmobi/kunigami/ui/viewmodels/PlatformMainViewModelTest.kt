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

package com.rwmobi.kunigami.ui.viewmodels

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("TooManyFunctions")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
class PlatformMainViewModelTest {
    private lateinit var platformMainViewModel: PlatformMainViewModel
    private lateinit var userPreferencesRepository: FakeUserPreferencesRepository
    private val preferredSize = DpSize(1024.dp, 768.dp)

    @BeforeTest
    fun setupViewModel() {
        val dispatcher = UnconfinedTestDispatcher()
        userPreferencesRepository = FakeUserPreferencesRepository()
        userPreferencesRepository.windowSize = preferredSize

        platformMainViewModel = PlatformMainViewModel(
            userPreferencesRepository = userPreferencesRepository,
            dispatcher = dispatcher,
        )
    }

    @Test
    fun `init should set default window size if none is saved`() = runTest {
        val defaultSize = DpSize(800.dp, 560.dp)
        userPreferencesRepository.windowSize = null

        // This ViewModel has init block. We have to set it up manually again
        val preInitPlatformMainViewModel = PlatformMainViewModel(
            userPreferencesRepository = userPreferencesRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )

        val windowSize = preInitPlatformMainViewModel.windowSize.value
        assertEquals(defaultSize, windowSize)
    }

    @Test
    fun `init should load preferred window size`() = runTest {
        userPreferencesRepository.windowSize = preferredSize

        // This ViewModel has init block. We have to set it up manually again
        val preInitPlatformMainViewModel = PlatformMainViewModel(
            userPreferencesRepository = userPreferencesRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )

        val windowSize = preInitPlatformMainViewModel.windowSize.value
        assertEquals(preferredSize, windowSize)
    }

    @Test
    fun `cachePreferredWindowSize should update preferred window size`() = runTest {
        val newSize = DpSize(1280.dp, 720.dp)

        platformMainViewModel.cachePreferredWindowSize(newSize)

        val savedSize = userPreferencesRepository.getWindowSize()
        assertNotNull(savedSize)
        assertEquals(newSize, savedSize)
    }

    @Test
    fun `window size should be updated when cachePreferredWindowSize is called`() = runTest {
        val newSize = DpSize(1280.dp, 720.dp)

        platformMainViewModel.cachePreferredWindowSize(newSize)

        val updatedSize = platformMainViewModel.windowSize.value
        assertEquals(newSize, userPreferencesRepository.windowSize)
        assertEquals(newSize, updatedSize)
    }
}
