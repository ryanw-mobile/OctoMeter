/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.rwmobi.kunigami.ui.test.ComposePagerTestRule
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.content_description_navigation_bar
import kunigami.composeapp.generated.resources.content_description_navigation_rail
import org.jetbrains.compose.resources.getString

internal class MainActivityTestRobot(
    private val composeTestRule: ComposePagerTestRule,
) {
    // Actions
    fun printSemanticTree() {
        with(composeTestRule) {
            onRoot().printToLog("SemanticTree")
        }
    }

    // Assertions
    suspend fun assertNavigationBarIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = getString(resource = Res.string.content_description_navigation_bar)).assertIsDisplayed()
        }
    }

    suspend fun assertNavigationRailIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = getString(resource = Res.string.content_description_navigation_rail)).assertIsDisplayed()
        }
    }
}
