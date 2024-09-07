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
