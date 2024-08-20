/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
        // ViewModel is set up with preferred size initialised
        val windowSize = platformMainViewModel.windowSize.value
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
