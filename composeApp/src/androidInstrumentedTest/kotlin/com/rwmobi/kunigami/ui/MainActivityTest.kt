/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.kunigami.MainActivity
import kotlinx.coroutines.runBlocking
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

    @Before
    fun setUp() {
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
    }

    @Test
    fun appLaunchSuccessfully() =
        runBlocking {
            with(mainActivityTestRobot) {
            }
        }
}
