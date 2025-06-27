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

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeter
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

object AccountSampleData {
    val accountA1234A1B1 = Account(
        accountNumber = "A-1234A1B1",
        preferredName = "Ryan",
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
                        deviceId = "FF-FF-FF-FF-FF-FF-FF-FF",
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
        preferredName = "Ryan",
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
                        deviceId = "FF-FF-FF-FF-FF-FF-FF-01",
                        makeAndType = null,
                        readingSource = null,
                        readAt = null,
                        value = null,
                    ),
                    ElectricityMeter(
                        serialNumber = "2222222222",
                        deviceId = "FF-FF-FF-FF-FF-FF-FF-02",
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
