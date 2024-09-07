/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.previewsampledata

import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal object TariffSamples {
    val agileFlex221125 = Tariff(
        productCode = "AGILE-FLEX-22-11-25",
        tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        isVariable = false,
        vatInclusiveStandardUnitRate = 99.257.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 94.682.roundToTwoDecimalPlaces(),
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        availability = Clock.System.now()..Instant.DISTANT_FUTURE,
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
    )

    val var221101 = Tariff(
        productCode = "VAR-22-11-01",
        fullName = "Flexible Octopus",
        displayName = "Flexible Octopus",
        description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
        tariffCode = "E-1R-VAR-22-11-01-A",
        isVariable = true,
        availability = Instant.parse("2023-03-28T00:00:00Z")..Instant.DISTANT_FUTURE,
        vatInclusiveStandardUnitRate = 25.2546.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 47.8485.roundToTwoDecimalPlaces(),
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
    )

    val fix12M240411 = Tariff(
        productCode = "OE-FIX-12M-24-04-11",
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        description = "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
        tariffCode = "E-1R-OE-FIX-12M-24-04-11-A",
        isVariable = false,
        availability = Instant.parse("2024-04-11T00:00:00+01:00")..Instant.parse("2024-05-04T00:00:00+01:00"),
        vatInclusiveStandardUnitRate = 24.5112.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 47.8485.roundToTwoDecimalPlaces(),
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
    )
}
