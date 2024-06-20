/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.previewsampledata

import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal object TariffSamples {
    val agileFlex221125 = TariffSummary(
        productCode = "AGILE-FLEX-22-11-25",
        tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        isVariable = false,
        vatInclusiveUnitRate = 99.257.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 94.682.roundToTwoDecimalPlaces(),
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        availability = Clock.System.now()..Instant.DISTANT_FUTURE,
    )

    val var221101 = TariffSummary(
        productCode = "VAR-22-11-01",
        fullName = "Flexible Octopus",
        displayName = "Flexible Octopus",
        description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
        tariffCode = "E-1R-VAR-22-11-01-A",
        isVariable = true,
        availability = Instant.parse("2023-03-28T00:00:00Z")..Instant.DISTANT_FUTURE,
        vatInclusiveUnitRate = 25.2546.roundToTwoDecimalPlaces(),
        vatInclusiveStandingCharge = 47.8485.roundToTwoDecimalPlaces(),
    )
}
