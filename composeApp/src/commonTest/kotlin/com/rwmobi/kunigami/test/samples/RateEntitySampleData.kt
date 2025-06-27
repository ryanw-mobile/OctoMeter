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

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlin.time.Instant

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
