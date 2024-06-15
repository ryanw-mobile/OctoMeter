/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
