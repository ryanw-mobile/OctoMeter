/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.data.source.local.database.entity.RateEntity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.data.source.network.dto.prices.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

object GetStandardUnitRatesSampleData {
    val json = """{
  "count": 5,
  "next": null,
  "previous": null,
  "results": [
    {
      "value_exc_vat": 17.42,
      "value_inc_vat": 18.291,
      "valid_from": "2024-05-07T21:30:00Z",
      "valid_to": "2024-05-07T22:00:00Z",
      "payment_method": null
    },
    {
      "value_exc_vat": 17.86,
      "value_inc_vat": 18.753,
      "valid_from": "2024-05-07T21:00:00Z",
      "valid_to": "2024-05-07T21:30:00Z",
      "payment_method": null
    },
    {
      "value_exc_vat": 17.42,
      "value_inc_vat": 18.291,
      "valid_from": "2024-05-07T20:30:00Z",
      "valid_to": "2024-05-07T21:00:00Z",
      "payment_method": null
    },
    {
      "value_exc_vat": 19.23,
      "value_inc_vat": 20.1915,
      "valid_from": "2024-05-07T20:00:00Z",
      "valid_to": "2024-05-07T20:30:00Z",
      "payment_method": null
    },
    {
      "value_exc_vat": 21.47,
      "value_inc_vat": 22.5435,
      "valid_from": "2024-05-07T19:30:00Z",
      "valid_to": "2024-05-07T20:00:00Z",
      "payment_method": null
    }
  ]
}
    """.trimIndent()

    val dto = PricesApiResponse(
        count = 5,
        next = null,
        previous = null,
        results = listOf(
            RateDto(
                vatExclusivePrice = 17.42,
                vatInclusivePrice = 18.291,
                validFrom = Instant.parse("2024-05-07T21:30:00Z"),
                validTo = Instant.parse("2024-05-07T22:00:00Z"),
                paymentMethod = null,
            ),
            RateDto(
                vatExclusivePrice = 17.86,
                vatInclusivePrice = 18.753,
                validFrom = Instant.parse("2024-05-07T21:00:00Z"),
                validTo = Instant.parse("2024-05-07T21:30:00Z"),
                paymentMethod = null,
            ),
            RateDto(
                vatExclusivePrice = 17.42,
                vatInclusivePrice = 18.291,
                validFrom = Instant.parse("2024-05-07T20:30:00Z"),
                validTo = Instant.parse("2024-05-07T21:00:00Z"),
                paymentMethod = null,
            ),
            RateDto(
                vatExclusivePrice = 19.23,
                vatInclusivePrice = 20.1915,
                validFrom = Instant.parse("2024-05-07T20:00:00Z"),
                validTo = Instant.parse("2024-05-07T20:30:00Z"),
                paymentMethod = null,
            ),
            RateDto(
                vatExclusivePrice = 21.47,
                vatInclusivePrice = 22.5435,
                validFrom = Instant.parse("2024-05-07T19:30:00Z"),
                validTo = Instant.parse("2024-05-07T20:00:00Z"),
                paymentMethod = null,
            ),
        ),
    )

    val rateEntity = listOf(
        RateEntity(
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-J",
            rateType = RateType.STANDARD_UNIT_RATE,
            vatRate = 18.291,
            validFrom = Instant.parse("2024-05-07T21:30:00Z"),
            validTo = Instant.parse("2024-05-07T22:00:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        RateEntity(
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-J",
            rateType = RateType.STANDARD_UNIT_RATE,
            vatRate = 18.753,
            validFrom = Instant.parse("2024-05-07T21:00:00Z"),
            validTo = Instant.parse("2024-05-07T21:30:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        RateEntity(
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-J",
            rateType = RateType.STANDARD_UNIT_RATE,
            vatRate = 18.291,
            validFrom = Instant.parse("2024-05-07T20:30:00Z"),
            validTo = Instant.parse("2024-05-07T21:00:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        RateEntity(
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-J",
            rateType = RateType.STANDARD_UNIT_RATE,
            vatRate = 20.1915,
            validFrom = Instant.parse("2024-05-07T20:00:00Z"),
            validTo = Instant.parse("2024-05-07T20:30:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        RateEntity(
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-J",
            rateType = RateType.STANDARD_UNIT_RATE,
            vatRate = 22.5435,
            validFrom = Instant.parse("2024-05-07T19:30:00Z"),
            validTo = Instant.parse("2024-05-07T20:00:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
    )

    val rate = listOf(
        Rate(
            vatInclusivePrice = 18.291,
            validity = Instant.parse("2024-05-07T21:30:00Z")..Instant.parse("2024-05-07T22:00:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        Rate(
            vatInclusivePrice = 18.753,
            validity = Instant.parse("2024-05-07T21:00:00Z")..Instant.parse("2024-05-07T21:30:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        Rate(
            vatInclusivePrice = 18.291,
            validity = Instant.parse("2024-05-07T20:30:00Z")..Instant.parse("2024-05-07T21:00:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        Rate(
            vatInclusivePrice = 20.1915,
            validity = Instant.parse("2024-05-07T20:00:00Z")..Instant.parse("2024-05-07T20:30:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
        Rate(
            vatInclusivePrice = 22.5435,
            validity = Instant.parse("2024-05-07T19:30:00Z")..Instant.parse("2024-05-07T20:00:00Z"),
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
    )
}
