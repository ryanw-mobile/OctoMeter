/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.samples.TariffSampleData
import com.rwmobi.kunigami.ui.model.product.RetailRegion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TariffTest {
    private val tariffCode1125A = "E-1R-AGILE-FLEX-22-11-25-A"
    private val tariffCode0411C = "E-2R-OE-FIX-12M-24-04-11-C"
    private val tariffCode1101A = "E-2R-VAR-22-11-01-A"
    private val tariffCode1101P = "E-2R-VAR-22-11-01-P"

    @Test
    fun `extractProductCode should return correct product code`() {
        assertEquals("AGILE-FLEX-22-11-25", Tariff.extractProductCode(tariffCode1125A))
        assertEquals("OE-FIX-12M-24-04-11", Tariff.extractProductCode(tariffCode0411C))
        assertEquals("VAR-22-11-01", Tariff.extractProductCode(tariffCode1101A))

        val invalidTariffCode = "E-1R"
        assertNull(Tariff.extractProductCode(invalidTariffCode))
    }

    @Test
    fun `getRetailRegion should return correct retail region`() {
        assertEquals(RetailRegion.EASTERN_ENGLAND, Tariff.getRetailRegion(tariffCode1125A))
        assertEquals(RetailRegion.LONDON, Tariff.getRetailRegion(tariffCode0411C))
        assertEquals(RetailRegion.NORTHERN_SCOTLAND, Tariff.getRetailRegion(tariffCode1101P))

        val invalidTariffCode = "E-1R-AGILE-FLEX-22-11-25-1"
        assertNull(Tariff.getRetailRegion(invalidTariffCode))
    }

    @Test
    fun `isSingleRate should return true for single rate tariff code`() {
        val singleRateTariffCode = tariffCode1125A
        assertTrue(Tariff.isSingleRate(singleRateTariffCode))

        val nonSingleRateTariffCode = tariffCode0411C
        assertFalse(Tariff.isSingleRate(nonSingleRateTariffCode))

        val invalidTariffCode = "E-R"
        assertFalse(Tariff.isSingleRate(invalidTariffCode))
    }

    @Test
    fun `extractProductCode instance method should return correct product code`() {
        val tariffSummary = TariffSampleData.agileFlex221125
        assertEquals("AGILE-FLEX-22-11-25", tariffSummary.extractProductCode())
    }

    @Test
    fun `getRetailRegion instance method should return correct retail region`() {
        val tariffSummary = TariffSampleData.agileFlex221125
        assertEquals(RetailRegion.EASTERN_ENGLAND, tariffSummary.getRetailRegion())
    }

    @Test
    fun `isSingleRate instance method should return true for single rate tariff code`() {
        val tariffSummary = TariffSampleData.agileFlex221125
        assertTrue(tariffSummary.isSingleRate())
    }
}
