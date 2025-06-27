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

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import kotlin.time.Instant

object ConsumptionEntitySampleData {
    val sample1 = ConsumptionEntity(
        deviceId = "01-02-03-04-05-06-07-08",
        intervalStart = Instant.parse("2023-06-01T01:00:00Z"),
        intervalEnd = Instant.parse("2023-06-01T01:30:00Z"),
        kWhConsumed = 1.25,
        consumptionCost = 2.50,
        standingCharge = 1.16,
    )

    val sample2 = ConsumptionEntity(
        deviceId = "01-02-03-04-05-06-07-08",
        intervalStart = Instant.parse("2023-06-01T01:30:00Z"),
        intervalEnd = Instant.parse("2023-06-01T02:00:00Z"),
        kWhConsumed = 2.54,
        consumptionCost = 5.08,
        standingCharge = 1.16,
    )
}
