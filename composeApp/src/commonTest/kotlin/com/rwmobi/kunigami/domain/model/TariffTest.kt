/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import com.rwmobi.kunigami.domain.model.product.TariffSummary
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TariffTest {

    @Test
    fun `extractProductCode should return correct product code`() {
        val tariffCode1 = "E-1R-AGILE-FLEX-22-11-25-A"
        assertEquals("AGILE-FLEX-22-11-25", TariffSummary.extractProductCode(tariffCode1))

        val tariffCode2 = "E-2R-OE-FIX-12M-24-04-11-C"
        assertEquals("OE-FIX-12M-24-04-11", TariffSummary.extractProductCode(tariffCode2))

        val tariffCode3 = "E-2R-VAR-22-11-01-A"
        assertEquals("VAR-22-11-01", TariffSummary.extractProductCode(tariffCode3))

        val invalidTariffCode = "E-1R"
        assertNull(TariffSummary.extractProductCode(invalidTariffCode))
    }

    @Test
    fun `getRetailRegion should return correct retail region`() {
        val tariffCode1 = "E-1R-AGILE-FLEX-22-11-25-A"
        assertEquals("A", TariffSummary.getRetailRegion(tariffCode1))

        val tariffCode2 = "E-2R-OE-FIX-12M-24-04-11-C"
        assertEquals("C", TariffSummary.getRetailRegion(tariffCode2))

        val tariffCode3 = "E-2R-VAR-22-11-01-P"
        assertEquals("P", TariffSummary.getRetailRegion(tariffCode3))

        val invalidTariffCode = "E-1R-AGILE-FLEX-22-11-25-1"
        assertNull(TariffSummary.getRetailRegion(invalidTariffCode))
    }

    @Test
    fun `isSingleRate should return true for single rate tariff code`() {
        val singleRateTariffCode = "E-1R-AGILE-FLEX-22-11-25-A"
        assertTrue(TariffSummary.isSingleRate(singleRateTariffCode))

        val nonSingleRateTariffCode = "E-2R-OE-FIX-12M-24-04-11-C"
        assertFalse(TariffSummary.isSingleRate(nonSingleRateTariffCode))

        val invalidTariffCode = "E-R-VAR-22-11-01-A"
        assertFalse(TariffSummary.isSingleRate(invalidTariffCode))
    }

    @Test
    fun `extractProductCode instance method should return correct product code`() {
        val tariffSummary = TariffSummary(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
            availableFrom = Clock.System.now(),
            availableTo = null,
            isVariable = true,
        )
        assertEquals("AGILE-FLEX-22-11-25", tariffSummary.extractProductCode())
    }

    @Test
    fun `getRetailRegion instance method should return correct retail region`() {
        val tariffSummary = TariffSummary(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
            availableFrom = Clock.System.now(),
            availableTo = null,
            isVariable = true,
        )
        assertEquals("A", tariffSummary.getRetailRegion())
    }

    @Test
    fun `isSingleRate instance method should return true for single rate tariff code`() {
        val tariffSummary = TariffSummary(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
            availableFrom = Clock.System.now(),
            availableTo = null,
            isVariable = true,
        )
        assertTrue(tariffSummary.isSingleRate())
    }
}
