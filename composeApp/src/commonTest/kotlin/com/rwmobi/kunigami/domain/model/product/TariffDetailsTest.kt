/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TariffDetailsTest {
    private val tariffCode = "E-1R-GO-GREEN-VAR-22-10-14-A"

    @Test
    fun `hasStandardUnitRate should return true when standard unit rate is not null`() {
        val tariffDetails = TariffDetails(
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
        )
        assertTrue(tariffDetails.hasStandardUnitRate())
    }

    @Test
    fun `hasStandardUnitRate should return false when standard unit rate is null`() {
        val tariffDetails = TariffDetails(
            tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
            tariffCode = tariffCode,
            vatInclusiveStandingCharge = 100.0,
            vatInclusiveOnlineDiscount = 5.0,
            vatInclusiveDualFuelDiscount = 10.0,
            exitFeesType = ExitFeesType.NONE,
            vatInclusiveExitFees = 0.0,
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertFalse(tariffDetails.hasStandardUnitRate())
    }

    @Test
    fun `hasDualRates should return true when both day and night unit rates are not null`() {
        val tariffDetails = TariffDetails(
            tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
            tariffCode = tariffCode,
            vatInclusiveStandingCharge = 100.0,
            vatInclusiveOnlineDiscount = 5.0,
            vatInclusiveDualFuelDiscount = 10.0,
            exitFeesType = ExitFeesType.NONE,
            vatInclusiveExitFees = 0.0,
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertTrue(tariffDetails.hasDualRates())
    }

    @Test
    fun `hasDualRates should return false when day unit rate is null`() {
        val tariffDetails = TariffDetails(
            tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
            tariffCode = tariffCode,
            vatInclusiveStandingCharge = 100.0,
            vatInclusiveOnlineDiscount = 5.0,
            vatInclusiveDualFuelDiscount = 10.0,
            exitFeesType = ExitFeesType.NONE,
            vatInclusiveExitFees = 0.0,
            vatInclusiveStandardUnitRate = 20.0,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = 15.0,
        )
        assertFalse(tariffDetails.hasDualRates())
    }

    @Test
    fun `hasDualRates should return false when night unit rate is null`() {
        val tariffDetails = TariffDetails(
            tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
            tariffCode = tariffCode,
            vatInclusiveStandingCharge = 100.0,
            vatInclusiveOnlineDiscount = 5.0,
            vatInclusiveDualFuelDiscount = 10.0,
            exitFeesType = ExitFeesType.NONE,
            vatInclusiveExitFees = 0.0,
            vatInclusiveStandardUnitRate = 20.0,
            vatInclusiveDayUnitRate = 20.0,
            vatInclusiveNightUnitRate = null,
        )
        assertFalse(tariffDetails.hasDualRates())
    }
}
