/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import kotlinx.datetime.Instant

object ConsumptionEntitySampleData {
    val sample1 = ConsumptionEntity(
        meterSerial = "11A1234567",
        intervalStart = Instant.parse("2023-06-01T01:00:00Z"),
        intervalEnd = Instant.parse("2023-06-01T01:30:00Z"),
        kWhConsumed = 1.25,
    )

    val sample2 = ConsumptionEntity(
        meterSerial = "11A1234567",
        intervalStart = Instant.parse("2023-06-01T01:30:00Z"),
        intervalEnd = Instant.parse("2023-06-01T02:00:00Z"),
        kWhConsumed = 2.54,
    )
}
