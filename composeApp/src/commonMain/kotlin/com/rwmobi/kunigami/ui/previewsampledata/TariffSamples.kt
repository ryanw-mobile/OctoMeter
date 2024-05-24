/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.previewsampledata

import com.rwmobi.kunigami.domain.model.Tariff
import kotlinx.datetime.Clock

object TariffSamples {
    val agileFlex221125 = Tariff(
        productCode = "AGILE-FLEX-22-11-25",
        tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        vatInclusiveUnitRate = 99.257,
        vatInclusiveStandingCharge = 94.682,
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        availableFrom = Clock.System.now(),
        availableTo = null,
    )
}
