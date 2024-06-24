/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.samples

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
