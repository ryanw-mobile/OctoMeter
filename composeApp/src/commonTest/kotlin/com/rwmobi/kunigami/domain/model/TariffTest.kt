/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import io.kotest.matchers.shouldBe
import kotlinx.datetime.Clock
import kotlin.test.Test

class TariffTest {

    @Test
    fun `extractProductCode should return correct product code`() {
        val tariffCode1 = "E-1R-AGILE-FLEX-22-11-25-A"
        Tariff.extractProductCode(tariffCode1) shouldBe "AGILE-FLEX-22-11-25"

        val tariffCode2 = "E-2R-OE-FIX-12M-24-04-11-C"
        Tariff.extractProductCode(tariffCode2) shouldBe "OE-FIX-12M-24-04-11"

        val tariffCode3 = "E-2R-VAR-22-11-01-A"
        Tariff.extractProductCode(tariffCode3) shouldBe "VAR-22-11-01"

        val invalidTariffCode = "E-1R"
        Tariff.extractProductCode(invalidTariffCode) shouldBe null
    }

    @Test
    fun `getRetailRegion should return correct retail region`() {
        val tariffCode1 = "E-1R-AGILE-FLEX-22-11-25-A"
        Tariff.getRetailRegion(tariffCode1) shouldBe "A"

        val tariffCode2 = "E-2R-OE-FIX-12M-24-04-11-C"
        Tariff.getRetailRegion(tariffCode2) shouldBe "C"

        val tariffCode3 = "E-2R-VAR-22-11-01-P"
        Tariff.getRetailRegion(tariffCode3) shouldBe "P"

        val invalidTariffCode = "E-1R-AGILE-FLEX-22-11-25-1"
        Tariff.getRetailRegion(invalidTariffCode) shouldBe null
    }

    @Test
    fun `isSingleRate should return true for single rate tariff code`() {
        val singleRateTariffCode = "E-1R-AGILE-FLEX-22-11-25-A"
        Tariff.isSingleRate(singleRateTariffCode) shouldBe true

        val nonSingleRateTariffCode = "E-2R-OE-FIX-12M-24-04-11-C"
        Tariff.isSingleRate(nonSingleRateTariffCode) shouldBe false

        val invalidTariffCode = "E-R-VAR-22-11-01-A"
        Tariff.isSingleRate(invalidTariffCode) shouldBe false
    }

    @Test
    fun `extractProductCode instance method should return correct product code`() {
        val tariff = Tariff(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
            availableFrom = Clock.System.now(),
            availableTo = null,
        )
        tariff.extractProductCode() shouldBe "AGILE-FLEX-22-11-25"
    }

    @Test
    fun `getRetailRegion instance method should return correct retail region`() {
        val tariff = Tariff(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
            availableFrom = Clock.System.now(),
            availableTo = null,
        )
        tariff.getRetailRegion() shouldBe "A"
    }

    @Test
    fun `isSingleRate instance method should return true for single rate tariff code`() {
        val tariff = Tariff(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
            availableFrom = Clock.System.now(),
            availableTo = null,
        )
        tariff.isSingleRate() shouldBe true
    }
}
