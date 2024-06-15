/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.rate

import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentMethodTest {

    @Test
    fun `fromValue should return DIRECT_DEBIT for direct_debit`() {
        val method = PaymentMethod.fromValue("direct_debit")
        assertEquals(PaymentMethod.DIRECT_DEBIT, method)
    }

    @Test
    fun `fromValue should return NON_DIRECT_DEBIT for non_direct_debit`() {
        val method = PaymentMethod.fromValue("non_direct_debit")
        assertEquals(PaymentMethod.NON_DIRECT_DEBIT, method)
    }

    @Test
    fun `fromValue should return UNKNOWN for unknown value`() {
        val method = PaymentMethod.fromValue("some_unknown_value")
        assertEquals(PaymentMethod.UNKNOWN, method)
    }

    @Test
    fun `fromValue should return UNKNOWN for null value`() {
        val method = PaymentMethod.fromValue(null)
        assertEquals(PaymentMethod.UNKNOWN, method)
    }

    @Test
    fun `fromValue should return UNKNOWN for empty string`() {
        val method = PaymentMethod.fromValue("")
        assertEquals(PaymentMethod.UNKNOWN, method)
    }

    @Test
    fun `fromValue should return UNKNOWN for case insensitive match`() {
        val method = PaymentMethod.fromValue("DiReCt_DeBiT")
        assertEquals(PaymentMethod.DIRECT_DEBIT, method)
    }
}
