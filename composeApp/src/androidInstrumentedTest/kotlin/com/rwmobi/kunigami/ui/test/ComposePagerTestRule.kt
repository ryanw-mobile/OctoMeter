/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.test

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.rwmobi.kunigami.MainActivity

typealias ComposePagerTestRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
