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
