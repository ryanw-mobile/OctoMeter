/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object TariffSampleData {
    val agileFlex221125 = Tariff(
        productCode = "AGILE-FLEX-22-11-25",
        tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        isVariable = false,
        tariffActiveAt = Clock.System.now(),
        vatInclusiveStandardUnitRate = 99.257.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 94.682.roundToTwoDecimalPlaces(),
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        availability = Clock.System.now()..Instant.DISTANT_FUTURE,
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        vatInclusiveOnlineDiscount = 0.0,
        vatInclusiveDualFuelDiscount = 0.0,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
    )

    val var221101 = Tariff(
        productCode = "VAR-22-11-01",
        fullName = "Flexible Octopus",
        displayName = "Flexible Octopus",
        description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
        tariffCode = "E-1R-VAR-22-11-01-A",
        isVariable = true,
        tariffActiveAt = Clock.System.now(),
        availability = Instant.parse("2023-03-28T00:00:00Z")..Instant.DISTANT_FUTURE,
        vatInclusiveStandardUnitRate = 25.2546.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 47.8485.roundToTwoDecimalPlaces(),
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        vatInclusiveOnlineDiscount = 0.0,
        vatInclusiveDualFuelDiscount = 0.0,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
    )
}
