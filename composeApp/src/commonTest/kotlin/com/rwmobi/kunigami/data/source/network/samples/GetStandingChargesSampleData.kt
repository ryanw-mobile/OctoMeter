/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.samples

import com.rwmobi.kunigami.data.source.network.dto.prices.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import kotlinx.datetime.Instant

object GetStandingChargesSampleData {
    val json = """{
  "count": 8,
  "next": null,
  "previous": null,
  "results": [
    {
      "value_exc_vat": 45.57,
      "value_inc_vat": 47.8485,
      "valid_from": "2024-03-31T23:00:00Z",
      "valid_to": null,
      "payment_method": "DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 50.79,
      "value_inc_vat": 53.3295,
      "valid_from": "2024-03-31T23:00:00Z",
      "valid_to": null,
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 40.01,
      "value_inc_vat": 42.0105,
      "valid_from": "2024-01-01T00:00:00Z",
      "valid_to": "2024-03-31T23:00:00Z",
      "payment_method": "DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 45.23,
      "value_inc_vat": 47.4915,
      "valid_from": "2024-01-01T00:00:00Z",
      "valid_to": "2024-03-31T23:00:00Z",
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 40.0125,
      "value_inc_vat": 42.013125,
      "valid_from": "2023-03-31T23:00:00Z",
      "valid_to": "2024-01-01T00:00:00Z",
      "payment_method": "DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 45.2311,
      "value_inc_vat": 47.492655,
      "valid_from": "2023-03-31T23:00:00Z",
      "valid_to": "2024-01-01T00:00:00Z",
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 35.5166,
      "value_inc_vat": 37.29243,
      "valid_from": "2022-11-01T00:00:00Z",
      "valid_to": "2023-03-31T23:00:00Z",
      "payment_method": "DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 40.6283,
      "value_inc_vat": 42.659715,
      "valid_from": "2022-11-01T00:00:00Z",
      "valid_to": "2023-03-31T23:00:00Z",
      "payment_method": "NON_DIRECT_DEBIT"
    }
  ]
}
    """.trimIndent()

    val dto = PricesApiResponse(
        count = 8,
        next = null,
        previous = null,
        results = listOf(
            RateDto(
                vatExclusivePrice = 45.57,
                vatInclusivePrice = 47.8485,
                validFrom = Instant.parse("2024-03-31T23:00:00Z"),
                validTo = null,
                paymentMethod = "DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 50.79,
                vatInclusivePrice = 53.3295,
                validFrom = Instant.parse("2024-03-31T23:00:00Z"),
                validTo = null,
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 40.01,
                vatInclusivePrice = 42.0105,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-03-31T23:00:00Z"),
                paymentMethod = "DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 45.23,
                vatInclusivePrice = 47.4915,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-03-31T23:00:00Z"),
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 40.0125,
                vatInclusivePrice = 42.013125,
                validFrom = Instant.parse("2023-03-31T23:00:00Z"),
                validTo = Instant.parse("2024-01-01T00:00:00Z"),
                paymentMethod = "DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 45.2311,
                vatInclusivePrice = 47.492655,
                validFrom = Instant.parse("2023-03-31T23:00:00Z"),
                validTo = Instant.parse("2024-01-01T00:00:00Z"),
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 35.5166,
                vatInclusivePrice = 37.29243,
                validFrom = Instant.parse("2022-11-01T00:00:00Z"),
                validTo = Instant.parse("2023-03-31T23:00:00Z"),
                paymentMethod = "DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 40.6283,
                vatInclusivePrice = 42.659715,
                validFrom = Instant.parse("2022-11-01T00:00:00Z"),
                validTo = Instant.parse("2023-03-31T23:00:00Z"),
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
        ),
    )
}
