/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeter
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

object AccountSampleData {
    val accountA1234A1B1 = Account(
        accountNumber = "A-1234A1B1",
        fullAddress = "RW MobiMedia UK Limited\n2 Frederick Street\nKing's Cross\nLondon\nWC1X 0ND",
        postcode = "WC1X 0ND",
        movedInAt = Clock.System.now(),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "1200000345678",
                meters = listOf(
                    ElectricityMeter(
                        serialNumber = "11A1234567",
                        makeAndType = null,
                        readingSource = null,
                        readAt = null,
                        value = null,
                    ),
                ),
                agreements = listOf(
                    Agreement(
                        tariffCode = "E-1R-VAR-22-11-01-C",
                        period = Clock.System.now()..Clock.System.now().plus(Duration.parse("365d")),
                        fullName = "Flexible Octopus",
                        displayName = "Flexible Octopus",
                        description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
                        isHalfHourlyTariff = false,
                        vatInclusiveStandingCharge = 38.72,
                        vatInclusiveStandardUnitRate = 23.53,
                        vatInclusiveDayUnitRate = null,
                        vatInclusiveNightUnitRate = null,
                        vatInclusiveOffPeakRate = null,
                        agilePriceCap = null,
                    ),
                ),
            ),
        ),
    )

    val accountB1234A1A1 = Account(
        accountNumber = "B-1234A1A1",
        fullAddress = "10 downing street\nLondon\nW1 1AA",
        postcode = "W1 1AA",
        movedInAt = Instant.parse("2020-11-30T00:00:00Z"),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "1000000000000",
                meters = listOf(
                    ElectricityMeter(
                        serialNumber = "1111111111",
                        makeAndType = null,
                        readingSource = null,
                        readAt = null,
                        value = null,
                    ),
                    ElectricityMeter(
                        serialNumber = "2222222222",
                        makeAndType = null,
                        readingSource = null,
                        readAt = null,
                        value = null,
                    ),
                ),
                agreements = listOf(
                    Agreement(
                        tariffCode = "E-1R-VAR-20-09-22-N",
                        period = Instant.parse("2020-12-17T00:00:00Z")..Instant.parse("2021-12-17T00:00:00Z"),
                        fullName = "Sample tariff name",
                        displayName = "Sample display name",
                        description = "Sample description",
                        isHalfHourlyTariff = true,
                        vatInclusiveStandingCharge = 51.606,
                        vatInclusiveStandardUnitRate = null,
                        vatInclusiveDayUnitRate = null,
                        vatInclusiveNightUnitRate = null,
                        vatInclusiveOffPeakRate = null,
                        agilePriceCap = null,
                    ),
                    Agreement(
                        tariffCode = "E-1R-VAR-21-09-29-N",
                        period = Instant.parse("2021-12-17T00:00:00Z")..Instant.parse("2023-03-31T23:00:00Z"),
                        fullName = "Sample tariff name",
                        displayName = "Sample display name",
                        description = "Sample description",
                        isHalfHourlyTariff = false,
                        vatInclusiveStandingCharge = 96.304,
                        vatInclusiveStandardUnitRate = null,
                        vatInclusiveDayUnitRate = null,
                        vatInclusiveNightUnitRate = null,
                        vatInclusiveOffPeakRate = null,
                        agilePriceCap = null,
                    ),
                    Agreement(
                        tariffCode = "E-1R-VAR-22-11-01-N",
                        period = Instant.parse("2023-03-31T23:00:00Z")..Instant.DISTANT_FUTURE,
                        fullName = "Sample tariff name",
                        displayName = "Sample display name",
                        description = "Sample description",
                        isHalfHourlyTariff = false,
                        vatInclusiveStandingCharge = 17.601,
                        vatInclusiveStandardUnitRate = null,
                        vatInclusiveDayUnitRate = null,
                        vatInclusiveNightUnitRate = null,
                        vatInclusiveOffPeakRate = null,
                        agilePriceCap = null,
                    ),
                ),
            ),
        ),
    )
}
