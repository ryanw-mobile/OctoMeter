/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import com.rwmobi.kunigami.domain.samples.TariffSampleData
import com.rwmobi.kunigami.ui.model.product.RetailRegion
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class TariffTest {
    private val tariffCode1125A = "E-1R-AGILE-FLEX-22-11-25-A"
    private val tariffCode0411C = "E-2R-OE-FIX-12M-24-04-11-C"
    private val tariffCode1101A = "E-2R-VAR-22-11-01-A"
    private val tariffCode1101P = "E-2R-VAR-22-11-01-P"

    private val tariffCode = "E-1R-GO-GREEN-VAR-22-10-14-A"
    private val sampleTariff = Tariff(
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        tariffCode = tariffCode,
        vatInclusiveStandingCharge = 100.0,
        vatInclusiveOnlineDiscount = 5.0,
        vatInclusiveDualFuelDiscount = 10.0,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveStandardUnitRate = 20.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        productCode = "GO-GREEN-VAR-22-10-14",
        fullName = "Octopus Go Green October 2022 v1",
        displayName = "Octopus Go Green",
        description = "Octopus Go Green is our EV tariff exclusively available to Volkswagen Group EV drivers.",
        isVariable = true,
        availability = Instant.parse("2022-10-14T00:00:00+01:00")..Instant.DISTANT_FUTURE,
        tariffActiveAt = Instant.parse("2024-06-22T16:54:27.330542Z"),
    )

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
        val tariff = TariffSampleData.agileFlex221125
        assertEquals("AGILE-FLEX-22-11-25", tariff.extractProductCode())
    }

    @Test
    fun `getRetailRegion instance method should return correct retail region`() {
        val tariff = TariffSampleData.agileFlex221125
        assertEquals(RetailRegion.EASTERN_ENGLAND, tariff.getRetailRegion())
    }

    @Test
    fun `isSingleRate instance method should return true for single rate tariff code`() {
        val tariff = TariffSampleData.agileFlex221125
        assertTrue(tariff.isSingleRate())
    }

    @Test
    fun `hasStandardUnitRate should return true when standard unit rate is not null`() {
        val tariff = sampleTariff
        assertTrue(tariff.hasStandardUnitRate())
    }

    @Test
    fun `hasStandardUnitRate should return false when standard unit rate is null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
        )
        assertFalse(tariff.hasStandardUnitRate())
    }

    @Test
    fun `hasDualRates should return true when both day and night unit rates are not null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertTrue(tariff.hasDualRates())
    }

    @Test
    fun `hasDualRates should return false when day unit rate is null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = 20.0,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertFalse(tariff.hasDualRates())
    }

    @Test
    fun `hasDualRates should return false when night unit rate is null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = 20.0,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = null,
        )
        assertFalse(tariff.hasDualRates())
    }

    @Test
    fun `isAgileProduct should return true when product code contains AGILE`() {
        val tariff = TariffSampleData.agileFlex221125
        assertTrue(tariff.isAgileProduct())
    }

    @Test
    fun `isAgileProduct should return false when product code does not contain AGILE`() {
        val tariff = TariffSampleData.var221101
        assertFalse(tariff.isAgileProduct())
    }

    @Test
    fun `isSameTariff should return true when tariff code matches`() {
        val tariff = TariffSampleData.agileFlex221125
        assertTrue(tariff.isSameTariff(tariffCode = "E-1R-AGILE-FLEX-22-11-25-A"))
    }

    @Test
    fun `isSameTariff should return false when tariff code does not match`() {
        val tariff = TariffSampleData.agileFlex221125
        assertFalse(tariff.isSameTariff(tariffCode = "random-tariff-code"))
    }

    @Test
    fun `isSameTariff should return false when tariff code is null`() {
        val tariff = TariffSampleData.agileFlex221125
        assertFalse(tariff.isSameTariff(tariffCode = null))
    }
}
