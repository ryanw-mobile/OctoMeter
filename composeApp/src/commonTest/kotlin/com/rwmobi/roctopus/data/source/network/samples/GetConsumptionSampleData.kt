/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.source.network.samples

import com.rwmobi.roctopus.data.source.network.dto.ConsumptionApiResponse
import com.rwmobi.roctopus.data.source.network.dto.ConsumptionDto
import kotlinx.datetime.Instant

object GetConsumptionSampleData {
    val json = """{
  "count": 768,
  "next": "https://api.octopus.energy/v1/electricity-meter-points/1100000111111/meters/11L1111111/consumption/?page=2",
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
        count = 768,
        next = "https://api.octopus.energy/v1/electricity-meter-points/1100000111111/meters/11L1111111/consumption/?page=2",
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
}
