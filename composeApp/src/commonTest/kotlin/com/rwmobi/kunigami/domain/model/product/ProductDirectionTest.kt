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

package com.rwmobi.kunigami.domain.model.product

import kotlin.test.Test
import kotlin.test.assertEquals

class ProductDirectionTest {

    @Test
    fun `fromApiValue should return IMPORT for import`() {
        val direction = ProductDirection.fromApiValue("import")
        assertEquals(ProductDirection.IMPORT, direction)
    }

    @Test
    fun `fromApiValue should return EXPORT for export`() {
        val direction = ProductDirection.fromApiValue("export")
        assertEquals(ProductDirection.EXPORT, direction)
    }

    @Test
    fun `fromApiValue should return UNKNOWN for unknown value`() {
        val direction = ProductDirection.fromApiValue("some_unknown_value")
        assertEquals(ProductDirection.UNKNOWN, direction)
    }

    @Test
    fun `fromApiValue should return UNKNOWN for null value`() {
        val direction = ProductDirection.fromApiValue(null)
        assertEquals(ProductDirection.UNKNOWN, direction)
    }

    @Test
    fun `fromApiValue should return UNKNOWN for empty string`() {
        val direction = ProductDirection.fromApiValue("")
        assertEquals(ProductDirection.UNKNOWN, direction)
    }

    @Test
    fun `fromApiValue should return correct direction for case insensitive match`() {
        val directionImport = ProductDirection.fromApiValue("ImPoRt")
        assertEquals(ProductDirection.IMPORT, directionImport)

        val directionExport = ProductDirection.fromApiValue("eXpOrT")
        assertEquals(ProductDirection.EXPORT, directionExport)
    }
}
