/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PostcodeStringExtensionsKtTest {

    @Test
    fun `isValidPostcodePattern should return true for valid A9 9AA format`() {
        val postcode = "A1 1AA"
        assertTrue(postcode.isValidPostcodePattern())
    }

    @Test
    fun `isValidPostcodePattern should return true for valid A99 9AA format`() {
        val postcode = "A12 1AA"
        assertTrue(postcode.isValidPostcodePattern())
    }

    @Test
    fun `isValidPostcodePattern should return true for valid AA9 9AA format`() {
        val postcode = "AB1 1AA"
        assertTrue(postcode.isValidPostcodePattern())
    }

    @Test
    fun `isValidPostcodePattern should return true for valid AA99 9AA format`() {
        val postcode = "AB12 1AA"
        assertTrue(postcode.isValidPostcodePattern())
    }

    @Test
    fun `isValidPostcodePattern should return false for invalid format`() {
        val postcode = "AB123 1AA"
        assertFalse(postcode.isValidPostcodePattern())
    }

    @Test
    fun `isValidPostcodePattern should return false for incorrect characters`() {
        val postcode = "AB!@ 1AA"
        assertFalse(postcode.isValidPostcodePattern())
    }

    @Test
    fun `formatPostcode should format valid A9 9AA format correctly`() {
        val postcode = "A11AA"
        val expected = "A1 1AA"
        assertEquals(expected, postcode.formatPostcode())
    }

    @Test
    fun `formatPostcode should format valid A99 9AA format correctly`() {
        val postcode = "A121AA"
        val expected = "A12 1AA"
        assertEquals(expected, postcode.formatPostcode())
    }

    @Test
    fun `formatPostcode should format valid AA9 9AA format correctly`() {
        val postcode = "AB11AA"
        val expected = "AB1 1AA"
        assertEquals(expected, postcode.formatPostcode())
    }

    @Test
    fun `formatPostcode should format valid AA99 9AA format correctly`() {
        val postcode = "AB121AA"
        val expected = "AB12 1AA"
        assertEquals(expected, postcode.formatPostcode())
    }

    @Test
    fun `formatPostcode should return original value for invalid postcode`() {
        val postcode = "AB123 1AA"
        assertEquals(postcode, postcode.formatPostcode())
    }
}
