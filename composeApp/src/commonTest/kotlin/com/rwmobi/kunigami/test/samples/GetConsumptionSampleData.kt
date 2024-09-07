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

import com.rwmobi.kunigami.data.source.network.dto.consumption.ConsumptionApiResponse
import com.rwmobi.kunigami.data.source.network.dto.consumption.ConsumptionDto
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import kotlinx.datetime.Instant

object GetConsumptionSampleData {
    val json = """{
  "count": 5,
  "next": null,
  "previous": null,
  "results": [
    {
      "consumption": 0.113,
      "interval_start": "2024-05-07T00:30:00+01:00",
      "interval_end": "2024-05-07T01:00:00+01:00"
    },
    {
      "consumption": 0.58,
      "interval_start": "2024-05-07T00:00:00+01:00",
      "interval_end": "2024-05-07T00:30:00+01:00"
    },
    {
      "consumption": 0.201,
      "interval_start": "2024-05-06T23:30:00+01:00",
      "interval_end": "2024-05-07T00:00:00+01:00"
    },
    {
      "consumption": 0.451,
      "interval_start": "2024-05-06T23:00:00+01:00",
      "interval_end": "2024-05-06T23:30:00+01:00"
    },
    {
      "consumption": 0.512,
      "interval_start": "2024-05-06T22:30:00+01:00",
      "interval_end": "2024-05-06T23:00:00+01:00"
    }
  ]
}
    """.trimIndent()

    val dto = ConsumptionApiResponse(
        count = 5,
        next = null,
        previous = null,
        results = listOf(
            ConsumptionDto(
                consumption = 0.113,
                intervalStart = Instant.parse("2024-05-06T23:30:00Z"),
                intervalEnd = Instant.parse("2024-05-07T00:00:00Z"),
            ),
            ConsumptionDto(
                consumption = 0.58,
                intervalStart = Instant.parse("2024-05-06T23:00:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:30:00Z"),
            ),
            ConsumptionDto(
                consumption = 0.201,
                intervalStart = Instant.parse("2024-05-06T22:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T23:00:00Z"),
            ),
            ConsumptionDto(
                consumption = 0.451,
                intervalStart = Instant.parse("2024-05-06T22:00:00Z"),
                intervalEnd = Instant.parse("2024-05-06T22:30:00Z"),
            ),
            ConsumptionDto(
                consumption = 0.512,
                intervalStart = Instant.parse("2024-05-06T21:30:00Z"),
                intervalEnd = Instant.parse("2024-05-06T22:00:00Z"),
            ),
        ),
    )

    val consumption = listOf(
        Consumption(
            kWhConsumed = 0.11,
            interval = Instant.parse("2024-05-06T23:30:00Z")..Instant.parse("2024-05-07T00:00:00Z"),
        ),
        Consumption(
            kWhConsumed = 0.58,
            interval = Instant.parse("2024-05-06T23:00:00Z")..Instant.parse("2024-05-06T23:30:00Z"),
        ),
        Consumption(
            kWhConsumed = 0.2,
            interval = Instant.parse("2024-05-06T22:30:00Z")..Instant.parse("2024-05-06T23:00:00Z"),
        ),
        Consumption(
            kWhConsumed = 0.45,
            interval = Instant.parse("2024-05-06T22:00:00Z")..Instant.parse("2024-05-06T22:30:00Z"),
        ),
        Consumption(
            kWhConsumed = 0.51,
            interval = Instant.parse("2024-05-06T21:30:00Z")..Instant.parse("2024-05-06T22:00:00Z"),
        ),
    )
}
