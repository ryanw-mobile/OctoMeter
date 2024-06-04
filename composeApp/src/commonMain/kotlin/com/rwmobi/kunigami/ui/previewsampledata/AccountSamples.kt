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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

internal object AccountSamples {
    val account928 = Account(
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
    )

    val accountTwoElectricityMeterPoint = Account(
        id = 8638,
        accountNumber = "A-1234A1B1",
        fullAddress = "Address line 1\nAddress line 2\nAddress line 3\nAddress line 4",
        movedInAt = Clock.System.now(),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "1200000345678",
                meterSerialNumbers = listOf("11A1234567", "11A12345A7"),
                currentAgreement = Agreement(
                    tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                    validFrom = Clock.System.now(),
                    validTo = Clock.System.now().plus(Duration.parse("365d")),
                ),
            ),
            ElectricityMeterPoint(
                mpan = "1200000345670",
                meterSerialNumbers = listOf("11A1234560"),
                currentAgreement = Agreement(
                    tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                    validFrom = Clock.System.now(),
                    validTo = Clock.System.now().plus(Duration.parse("365d")),
                ),
            ),
        ),
    )
}
