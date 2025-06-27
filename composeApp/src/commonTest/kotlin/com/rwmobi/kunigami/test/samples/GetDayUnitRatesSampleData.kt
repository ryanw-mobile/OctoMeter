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

import com.rwmobi.kunigami.data.source.network.dto.prices.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import kotlin.time.Instant

object GetDayUnitRatesSampleData {
    val json = """{
  "count": 4,
  "next": null,
  "previous": null,
  "results": [
    {
      "value_exc_vat": 30.008,
      "value_inc_vat": 31.5084,
      "valid_from": "2024-03-31T23:00:00Z",
      "valid_to": null,
      "payment_method": "DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 30.654,
      "value_inc_vat": 32.1867,
      "valid_from": "2024-03-31T23:00:00Z",
      "valid_to": null,
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 36.144,
      "value_inc_vat": 37.9512,
      "valid_from": "2024-01-01T00:00:00Z",
      "valid_to": "2024-03-31T23:00:00Z",
      "payment_method": "NON_DIRECT_DEBIT"
    },
    {
      "value_exc_vat": 35.414,
      "value_inc_vat": 37.1847,
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
                vatExclusivePrice = 30.008,
                vatInclusivePrice = 31.5084,
                validFrom = Instant.parse("2024-03-31T23:00:00Z"),
                validTo = null,
                paymentMethod = "DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 30.654,
                vatInclusivePrice = 32.1867,
                validFrom = Instant.parse("2024-03-31T23:00:00Z"),
                validTo = null,
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 36.144,
                vatInclusivePrice = 37.9512,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-03-31T23:00:00Z"),
                paymentMethod = "NON_DIRECT_DEBIT",
            ),
            RateDto(
                vatExclusivePrice = 35.414,
                vatInclusivePrice = 37.1847,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-03-31T23:00:00Z"),
                paymentMethod = "DIRECT_DEBIT",
            ),
        ),
    )
}
