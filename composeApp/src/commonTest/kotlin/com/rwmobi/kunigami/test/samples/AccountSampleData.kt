/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

object AccountSampleData {
    val accountA1234A1B1 = Account(
        id = 8638,
        accountNumber = "A-1234A1B1",
        fullAddress = "RW MobiMedia UK Limited\n2 Frederick Street\nKing's Cross\nLondon\nWC1X 0ND",
        postcode = "WC1X 0ND",
        movedInAt = Clock.System.now(),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "1200000345678",
                meterSerialNumbers = listOf("11A1234567"),
                agreements = listOf(
                    Agreement(
                        tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                        period = Clock.System.now()..Clock.System.now().plus(Duration.parse("365d")),
                    ),
                ),
            ),
        ),
    )

    val accountB1234A1A1 = Account(
        id = 1234567,
        accountNumber = "B-1234A1A1",
        fullAddress = "10 downing street\nLondon\nW1 1AA",
        postcode = "W1 1AA",
        movedInAt = Instant.parse("2020-11-30T00:00:00Z"),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "1000000000000",
                meterSerialNumbers = listOf("1111111111", "2222222222"),
                agreements = listOf(
                    Agreement(tariffCode = "E-1R-VAR-20-09-22-N", period = Instant.parse("2020-12-17T00:00:00Z")..Instant.parse("2021-12-17T00:00:00Z")),
                    Agreement(tariffCode = "E-1R-VAR-21-09-29-N", period = Instant.parse("2021-12-17T00:00:00Z")..Instant.parse("2023-03-31T23:00:00Z")),
                    Agreement(tariffCode = "E-1R-VAR-22-11-01-N", period = Instant.parse("2023-03-31T23:00:00Z")..Instant.DISTANT_FUTURE),
                ),
            ),
        ),
    )
}
