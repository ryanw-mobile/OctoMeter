/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import com.rwmobi.kunigami.test.samples.TariffSampleData
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
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveStandardUnitRate = 20.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
        productCode = "GO-GREEN-VAR-22-10-14",
        fullName = "Octopus Go Green October 2022 v1",
        displayName = "Octopus Go Green",
        description = "Octopus Go Green is our EV tariff exclusively available to Volkswagen Group EV drivers.",
        isVariable = true,
        availability = Instant.parse("2022-10-14T00:00:00+01:00")..Instant.DISTANT_FUTURE,
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
    fun `isSingleFuel should return true for single rate tariff code`() {
        val singleRateTariffCode = tariffCode1125A
        assertTrue(Tariff.isSingleFuel(singleRateTariffCode))

        val nonSingleRateTariffCode = tariffCode0411C
        assertFalse(Tariff.isSingleFuel(nonSingleRateTariffCode))

        val invalidTariffCode = "E-R"
        assertFalse(Tariff.isSingleFuel(invalidTariffCode))
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
    fun `isSingleFuel instance method should return true for single fuel tariff code`() {
        val tariff = TariffSampleData.agileFlex221125
        assertTrue(tariff.isSingleFuel())
    }

    @Test
    fun `getElectricityTariffType should return STANDARD when standard unit rate is not null`() {
        val tariff = sampleTariff
        assertEquals(tariff.getElectricityTariffType(), ElectricityTariffType.STANDARD)
    }

    @Test
    fun `getElectricityTariffType should return UNKNOWN when all unit rates are null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveOffPeakRate = null,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = null,
        )
        assertEquals(tariff.getElectricityTariffType(), ElectricityTariffType.UNKNOWN)
    }

    @Test
    fun `getElectricityTariffType should return DAY_NIGHT when both day and night unit rates are not null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveOffPeakRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertEquals(tariff.getElectricityTariffType(), ElectricityTariffType.DAY_NIGHT)
    }

    @Test
    fun `getElectricityTariffType should return UNKMOWN when either day or night unit rate is null`() {
        val tariffDayNull = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveOffPeakRate = null,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertEquals(tariffDayNull.getElectricityTariffType(), ElectricityTariffType.UNKNOWN)

        val tariffNightNull = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveOffPeakRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = null,
        )
        assertEquals(tariffNightNull.getElectricityTariffType(), ElectricityTariffType.UNKNOWN)
    }

    @Test
    fun `getElectricityTariffType should return THREE_RATE when day off-peak and night unit rates are not null`() {
        val tariff = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = 15.0,
            vatInclusiveOffPeakRate = 10.0,
        )
        assertEquals(tariff.getElectricityTariffType(), ElectricityTariffType.THREE_RATE)
    }

    @Test
    fun `getElectricityTariffType should return UNKMOWN when off-peak is not null but either night or day is null`() {
        val tariffDayNull = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = 15.0,
            vatInclusiveOffPeakRate = 10.0,
        )
        assertEquals(tariffDayNull.getElectricityTariffType(), ElectricityTariffType.UNKNOWN)

        val tariffNightNull = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = null,
            vatInclusiveOffPeakRate = 10.0,
        )
        assertEquals(tariffNightNull.getElectricityTariffType(), ElectricityTariffType.UNKNOWN)

        val tariffDayNightNull = sampleTariff.copy(
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = null,
            vatInclusiveOffPeakRate = 10.0,
        )
        assertEquals(tariffDayNightNull.getElectricityTariffType(), ElectricityTariffType.UNKNOWN)
    }

    @Test
    fun `isAgileProduct should return true when product code contains AGILE`() {
        val tariff = TariffSampleData.agileFlex221125
        assertTrue(tariff.isAgileProduct())

        val tariffCodeResult = Tariff.isAgileProduct(tariffCode = TariffSampleData.agileFlex221125.tariffCode)
        assertTrue(tariffCodeResult)
    }

    @Test
    fun `isAgileProduct should return false when product code does not contain AGILE`() {
        val tariff = TariffSampleData.var221101
        assertFalse(tariff.isAgileProduct())

        val tariffCodeResult = Tariff.isAgileProduct(tariffCode = TariffSampleData.var221101.tariffCode)
        assertFalse(tariffCodeResult)
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
