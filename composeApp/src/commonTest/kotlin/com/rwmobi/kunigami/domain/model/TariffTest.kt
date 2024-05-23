/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import io.kotest.matchers.shouldBe
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
            code = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
        )
        tariff.extractProductCode() shouldBe "AGILE-FLEX-22-11-25"
    }

    @Test
    fun `getRetailRegion instance method should return correct retail region`() {
        val tariff = Tariff(
            code = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
        )
        tariff.getRetailRegion() shouldBe "A"
    }

    @Test
    fun `isSingleRate instance method should return true for single rate tariff code`() {
        val tariff = Tariff(
            code = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Agile Flex",
            displayName = "Agile Flex Tariff",
            vatInclusiveUnitRate = 15.5,
            vatInclusiveStandingCharge = 20.0,
        )
        tariff.isSingleRate() shouldBe true
    }
}
