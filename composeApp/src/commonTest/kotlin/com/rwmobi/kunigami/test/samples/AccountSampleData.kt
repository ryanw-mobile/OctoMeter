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
import kotlin.time.Duration

object AccountSampleData {
    val accountA1234A1B1 = Account(
        id = 8638,
        accountNumber = "A-1234A1B1",
        fullAddress = "Address line 1\nAddress line 2\nAddress line 3\nAddress line 4",
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
}
