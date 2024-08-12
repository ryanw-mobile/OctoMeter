/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.rwmobi.kunigami.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@kotlinx.coroutines.ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
    }

    @After
    fun tearDown() {
        uiDevice.setOrientationNatural()
    }

    /**
     * This test is designed for Renovate to make sure the App can launch without
     * crashes triggered by some known high-risk dependencies like Jetpack LifeCycle.
     */
    @Test
    fun navigationLayoutIsShownCorrectly() =
        runBlocking {
            with(mainActivityTestRobot) {
                // Make sure we start with Natural orientation
                uiDevice.setOrientationNatural()
                assertNavigationBarIsDisplayed()

                composeTestRule.waitForIdle()

                // Rotate to landscape
                uiDevice.setOrientationLeft()
                assertNavigationRailIsDisplayed()

                composeTestRule.waitForIdle()

                // Rotate to portrait
                uiDevice.setOrientationNatural()
                assertNavigationBarIsDisplayed()
            }
        }
}
