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

import com.rwmobi.kunigami.domain.extensions.atStartOfDay
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeter
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

internal object AccountSamples {
    val agreementE1RVAR221101C = Agreement(
        tariffCode = "E-1R-VAR-22-11-01-C",
        period = Instant.parse("2022-03-28T00:00:00Z")..Instant.parse("2024-04-11T00:00:00Z"),
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
    )

    val agreementE1RVAR231101C = Agreement(
        tariffCode = "E-1R-OE-FIX-12M-24-04-11-C",
        period = Instant.parse("2024-04-11T00:00:00Z")..Instant.DISTANT_FUTURE,
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        description = "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
        isHalfHourlyTariff = false,
        vatInclusiveStandingCharge = 38.72,
        vatInclusiveStandardUnitRate = 24.98,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
        agilePriceCap = null,
    )

    // This account is used in demo mode - usage. Needs to have sensible values.
    val account928 = Account(
        accountNumber = "A-9009A9A9",
        preferredName = "Ryan",
        fullAddress = "RW MobiMedia UK Limited\n2 Frederick Street\nKing's Cross\nLondon\nWC1X 0ND",
        postcode = "WC1X 0ND",
        movedInAt = Instant.parse("2022-03-28T00:00:00Z"),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "9900000999999",
                meters = listOf(
                    ElectricityMeter(
                        serialNumber = "99A9999999",
                        deviceId = "FF-FF-FF-FF-FF-FF-FF-FF",
                        makeAndType = "Sample Meter",
                        readingSource = null,
                        readAt = null,
                        value = null,
                    ),
                ),
                agreements = listOf(
                    agreementE1RVAR221101C,
                    agreementE1RVAR231101C,
                ),
            ),
        ),
    )

    val accountTwoElectricityMeterPoint = Account(
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
                        makeAndType = "Landis & Gyr E470",
                        readingSource = "Smart reading",
                        readAt = Clock.System.now().atStartOfDay(),
                        value = 12345.6789,
                    ),
                    ElectricityMeter(
                        serialNumber = "11A12345A7",
                        deviceId = "FF-FF-FF-FF-FF-FF-FF-FF",
                        makeAndType = "Landis & Gyr E470",
                        readingSource = "Smart reading",
                        readAt = Clock.System.now().atStartOfDay(),
                        value = 23456.789,
                    ),
                ),
                agreements = listOf(
                    agreementE1RVAR221101C.copy(
                        period = Clock.System.now()..Clock.System.now().plus(Duration.parse("365d")),
                    ),
                ),
            ),
            ElectricityMeterPoint(
                mpan = "1200000345670",
                meters = listOf(
                    ElectricityMeter(
                        serialNumber = "11A1234560",
                        deviceId = "FF-FF-FF-FF-FF-FF-FF-FF",
                        makeAndType = "Landis & Gyr E470",
                        readingSource = "Smart reading",
                        readAt = Clock.System.now().atStartOfDay(),
                        value = 10000.0,
                    ),
                ),
                agreements = listOf(
                    agreementE1RVAR231101C.copy(
                        period = Clock.System.now()..Clock.System.now().plus(Duration.parse("365d")),
                    ),
                ),
            ),
        ),
    )
}
