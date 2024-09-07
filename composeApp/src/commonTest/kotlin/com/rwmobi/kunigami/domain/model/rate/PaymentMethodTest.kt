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
