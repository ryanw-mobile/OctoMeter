/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant

object RateEntitySampleData {
    val standingChargeSample1 = RateEntity(
        tariffCode = "E-1R-AGILE-24-04-03-A",
        rateType = RateType.STANDING_CHARGE,
        paymentMethod = PaymentMethod.UNKNOWN,
        validFrom = Instant.parse("2024-04-01T23:00:00Z"),
        validTo = Instant.parse("2024-04-02T23:00:00Z"),
        vatRate = 46.8485,
    )

    val standingChargeSample2 = RateEntity(
        tariffCode = "E-1R-AGILE-24-04-03-A",
        rateType = RateType.STANDING_CHARGE,
        paymentMethod = PaymentMethod.UNKNOWN,
        validFrom = Instant.parse("2024-04-02T23:00:00Z"),
        validTo = Instant.DISTANT_FUTURE,
        vatRate = 47.8485,
    )

    val standardUnitRateSample1 = RateEntity(
        tariffCode = "E-1R-AGILE-24-04-03-A",
        rateType = RateType.STANDARD_UNIT_RATE,
        paymentMethod = PaymentMethod.UNKNOWN,
        validFrom = Instant.parse("2024-06-28T20:30:00Z"),
        validTo = Instant.parse("2024-06-28T21:00:00Z"),
        vatRate = 21.3885,
    )

    val standardUnitRateSample2 = RateEntity(
        tariffCode = "E-1R-AGILE-24-04-03-A",
        rateType = RateType.STANDARD_UNIT_RATE,
        paymentMethod = PaymentMethod.UNKNOWN,
        validFrom = Instant.parse("2024-06-28T21:00:00Z"),
        validTo = Instant.parse("2024-06-28T21:30:00Z"),
        vatRate = 21.3885,
    )

    val standardUnitRateSample3 = RateEntity(
        tariffCode = "E-1R-AGILE-24-04-03-A",
        rateType = RateType.STANDARD_UNIT_RATE,
        paymentMethod = PaymentMethod.UNKNOWN,
        validFrom = Instant.parse("2024-06-28T21:30:00Z"),
        validTo = Instant.parse("2024-06-28T22:00:00Z"),
        vatRate = 19.845,
    )
}
