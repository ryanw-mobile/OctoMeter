/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.samples.TariffSampleData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TariffTest {
    val tariffCode1125A = "E-1R-AGILE-FLEX-22-11-25-A"
    val tariffCode0411C = "E-2R-OE-FIX-12M-24-04-11-C"
    val tariffCode1101A = "E-2R-VAR-22-11-01-A"
    val tariffCode1101P = "E-2R-VAR-22-11-01-P"

    @Test
    fun `extractProductCode should return correct product code`() {
        assertEquals("AGILE-FLEX-22-11-25", TariffSummary.extractProductCode(tariffCode1125A))
        assertEquals("OE-FIX-12M-24-04-11", TariffSummary.extractProductCode(tariffCode0411C))
        assertEquals("VAR-22-11-01", TariffSummary.extractProductCode(tariffCode1101A))

        val invalidTariffCode = "E-1R"
        assertNull(TariffSummary.extractProductCode(invalidTariffCode))
    }

    @Test
    fun `getRetailRegion should return correct retail region`() {
        assertEquals("A", TariffSummary.getRetailRegion(tariffCode1125A))
        assertEquals("C", TariffSummary.getRetailRegion(tariffCode0411C))
        assertEquals("P", TariffSummary.getRetailRegion(tariffCode1101P))

        val invalidTariffCode = "E-1R-AGILE-FLEX-22-11-25-1"
        assertNull(TariffSummary.getRetailRegion(invalidTariffCode))
    }

    @Test
    fun `isSingleRate should return true for single rate tariff code`() {
        val singleRateTariffCode = tariffCode1125A
        assertTrue(TariffSummary.isSingleRate(singleRateTariffCode))

        val nonSingleRateTariffCode = tariffCode0411C
        assertFalse(TariffSummary.isSingleRate(nonSingleRateTariffCode))

        val invalidTariffCode = "E-R"
        assertFalse(TariffSummary.isSingleRate(invalidTariffCode))
    }

    @Test
    fun `extractProductCode instance method should return correct product code`() {
        val tariffSummary = TariffSampleData.agileFlex221125
        assertEquals("AGILE-FLEX-22-11-25", tariffSummary.extractProductCode())
    }

    @Test
    fun `getRetailRegion instance method should return correct retail region`() {
        val tariffSummary = TariffSampleData.agileFlex221125
        assertEquals("A", tariffSummary.getRetailRegion())
    }

    @Test
    fun `isSingleRate instance method should return true for single rate tariff code`() {
        val tariffSummary = TariffSampleData.agileFlex221125
        assertTrue(tariffSummary.isSingleRate())
    }
}
