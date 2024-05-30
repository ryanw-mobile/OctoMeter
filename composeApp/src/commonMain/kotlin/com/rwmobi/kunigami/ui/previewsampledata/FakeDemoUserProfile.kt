/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.previewsampledata

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import kotlinx.datetime.Instant

object FakeDemoUserProfile {
    val flexibleOctopusRegionADirectDebit = UserProfile(
        selectedMpan = "9900000999999",
        selectedMeterSerialNumber = "99A9999999",
        account = Account(
            id = 928,
            accountNumber = "A-9009A9A9",
            fullAddress = "RW MobiMedia UK Limited\n2 Frederick Street\nKing's Cross\nLondon\nWC1X 0ND",
            movedInAt = Instant.parse("2023-03-28T00:00:00Z"),
            movedOutAt = null,
            electricityMeterPoints = listOf(
                ElectricityMeterPoint(
                    mpan = "9900000999999",
                    meterSerialNumbers = listOf("99A9999999"),
                    currentAgreement = Agreement(
                        tariffCode = "E-1R-VAR-22-11-01-A",
                        validFrom = Instant.parse("2023-03-28T00:00:00Z"),
                        validTo = null,
                    ),
                ),
            ),
        ),
        tariffSummary = TariffSummary(
            productCode = "VAR-22-11-01",
            fullName = "Flexible Octopus",
            displayName = "Flexible Octopus",
            description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
            tariffCode = "E-1R-VAR-22-11-01-A",
            availableFrom = Instant.parse("2023-03-28T00:00:00Z"),
            availableTo = null,
            vatInclusiveUnitRate = 25.2546,
            vatInclusiveStandingCharge = 47.8485,
        ),
    )
}
