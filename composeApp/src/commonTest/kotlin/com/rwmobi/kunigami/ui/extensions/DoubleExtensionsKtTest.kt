/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import com.rwmobi.kunigami.ui.extensions.getPercentageColorIndex
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DoubleExtensionsKtTest {

    @Test
    fun `getPercentageColorIndex should return zero when value is zero`() {
        val maxValue = 100.0
        0.0.getPercentageColorIndex(maxValue) shouldBe 0
    }

    @Test
    fun `getPercentageColorIndex should return fifty when value is half of maxValue`() {
        val maxValue = 100.0
        50.0.getPercentageColorIndex(maxValue) shouldBe 50
    }

    @Test
    fun `getPercentageColorIndex should return ninety nine when value is maxValue`() {
        val maxValue = 100.0
        100.0.getPercentageColorIndex(maxValue) shouldBe 99
    }

    @Test
    fun `getPercentageColorIndex should return ninety nine when value is greater than maxValue`() {
        val maxValue = 100.0
        150.0.getPercentageColorIndex(maxValue) shouldBe 99
    }

    @Test
    fun `getPercentageColorIndex should return zero when value is negative`() {
        val maxValue = 100.0
        (-10.0).getPercentageColorIndex(maxValue) shouldBe 0
    }

    @Test
    fun `getPercentageColorIndex should return ninety nine when maxValue is zero`() {
        val maxValue = 0.0
        50.0.getPercentageColorIndex(maxValue) shouldBe 99 // Assuming maxValue 0 should result in 99
    }
}
