/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import kotlin.test.Test
import kotlin.test.assertEquals

class ExitFeesTypeTest {

    @Test
    fun `fromApiValue should return NONE for none`() {
        val type = ExitFeesType.fromApiValue("none")
        assertEquals(ExitFeesType.NONE, type)
    }

    @Test
    fun `fromApiValue should return UNKNOWN for unknown value`() {
        val type = ExitFeesType.fromApiValue("some_unknown_value")
        assertEquals(ExitFeesType.UNKNOWN, type)
    }

    @Test
    fun `fromApiValue should return UNKNOWN for null value`() {
        val type = ExitFeesType.fromApiValue(null)
        assertEquals(ExitFeesType.UNKNOWN, type)
    }

    @Test
    fun `fromApiValue should return UNKNOWN for empty string`() {
        val type = ExitFeesType.fromApiValue("")
        assertEquals(ExitFeesType.UNKNOWN, type)
    }

    @Test
    fun `fromApiValue should return NONE for case insensitive match`() {
        val type = ExitFeesType.fromApiValue("NoNe")
        assertEquals(ExitFeesType.NONE, type)
    }
}
