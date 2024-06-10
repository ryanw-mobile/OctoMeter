/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ListExtensionsKtTest {

    @Test
    fun `partitionList should return original list wrapped in a list when columns is zero`() {
        val list = listOf(1, 2, 3)
        val result = list.partitionList(0)
        result shouldHaveSize 1
        result.first() shouldBe list
    }

    @Test
    fun `partitionList should return original list wrapped in a list when columns is negative`() {
        val list = listOf(1, 2, 3)
        val result = list.partitionList(-1)
        result shouldHaveSize 1
        result.first() shouldBe list
    }

    @Test
    fun `partitionList should partition list into given number of columns`() {
        val list = listOf(1, 2, 3, 4, 5, 6)
        val result = list.partitionList(2)
        result shouldHaveSize 2
        result[0] shouldBe listOf(1, 2, 3)
        result[1] shouldBe listOf(4, 5, 6)
    }

    @Test
    fun `partitionList should handle list size not evenly divisible by columns`() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.partitionList(2)
        result shouldHaveSize 2
        result[0] shouldBe listOf(1, 2, 3)
        result[1] shouldBe listOf(4, 5)
    }

    @Test
    fun `partitionList should handle more columns than items`() {
        val list = listOf(1, 2, 3)
        val result = list.partitionList(4)
        result shouldHaveSize 4
        result[0] shouldBe listOf(1)
        result[1] shouldBe listOf(2)
        result[2] shouldBe listOf(3)
        result[3] shouldBe emptyList()
    }

    @Test
    fun `partitionList should handle empty list`() {
        val list = emptyList<Int>()
        val result = list.partitionList(3)
        result shouldHaveSize 3
        result.forEach { it shouldBe emptyList() }
    }

    @Test
    fun `partitionList should not remove the last column if columns is 2`() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.partitionList(2)
        result shouldHaveSize 2
        result[0] shouldBe listOf(1, 2, 3)
        result[1] shouldBe listOf(4, 5)
    }

    @Test
    fun `partitionList should remove the last empty column if columns is greater than 2`() {
        val list = listOf(1, 2, 3, 4)
        val result = list.partitionList(3)
        result shouldHaveSize 3
        result[0] shouldBe listOf(1, 2)
        result[1] shouldBe listOf(3, 4)
        result[2] shouldBe emptyList()
    }
}
