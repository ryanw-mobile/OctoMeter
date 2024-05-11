/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.samples

import com.rwmobi.kunigami.data.source.network.dto.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.RateDto
import kotlinx.datetime.Instant

object GetNightUnitRatesSampleData {
    val json = """{
  "count": 4,
  "next": null,
  "previous": null,
  "results": [
    {
      "value_exc_vat": 12.588,
      "value_inc_vat": 13.2174,
      "valid_from": "2024-03-31T23:00:00Z",
      "valid_to": null,
      "payment_method": "DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 12.859,
      "value_inc_vat": 13.50195,
      "valid_from": "2024-03-31T23:00:00Z",
      "valid_to": null,
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 15.527,
      "value_inc_vat": 16.30335,
      "valid_from": "2024-01-01T00:00:00Z",
      "valid_to": "2024-03-31T23:00:00Z",
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 14.856,
      "value_inc_vat": 15.5988,
      "valid_from": "2024-01-01T00:00:00Z",
      "valid_to": "2024-03-31T23:00:00Z",
      "payment_method": "DIRECT_DEBIT"
    }
  ]
}
    """.trimIndent()

    val dto = PricesApiResponse(
        count = 4,
        next = null,
        previous = null,
        results = listOf(
            RateDto(
                vatExclusivePrice = 12.588,
                vatInclusivePrice = 13.2174,
                validFrom = Instant.parse("2024-03-31T23:00:00Z"),
                validTo = null,
                paymentMethod = "DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 12.859,
                vatInclusivePrice = 13.50195,
                validFrom = Instant.parse("2024-03-31T23:00:00Z"),
                validTo = null,
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 15.527,
                vatInclusivePrice = 16.30335,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-03-31T23:00:00Z"),
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 14.856,
                vatInclusivePrice = 15.5988,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-03-31T23:00:00Z"),
                paymentMethod = "DIRECT_DEBIT",
            ),
        ),
    )
}
