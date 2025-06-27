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
import com.rwmobi.kunigami.data.source.network.dto.prices.PricesApiResponse
import com.rwmobi.kunigami.data.source.network.dto.prices.RateDto
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlin.time.Instant

object GetStandingChargesSampleData {
    val var221101Json = """{
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

    val var221101Dto = PricesApiResponse(
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

    val var221101RateEntity = listOf(
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 47.8485,
            validFrom = Instant.parse("2024-03-31T23:00:00Z"),
            validTo = null,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 53.3295,
            validFrom = Instant.parse("2024-03-31T23:00:00Z"),
            validTo = null,
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 42.0105,
            validFrom = Instant.parse("2024-01-01T00:00:00Z"),
            validTo = Instant.parse("2024-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 47.4915,
            validFrom = Instant.parse("2024-01-01T00:00:00Z"),
            validTo = Instant.parse("2024-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 42.013125,
            validFrom = Instant.parse("2023-03-31T23:00:00Z"),
            validTo = Instant.parse("2024-01-01T00:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 47.492655,
            validFrom = Instant.parse("2023-03-31T23:00:00Z"),
            validTo = Instant.parse("2024-01-01T00:00:00Z"),
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 37.29243,
            validFrom = Instant.parse("2022-11-01T00:00:00Z"),
            validTo = Instant.parse("2023-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        RateEntity(
            tariffCode = "E-1R-VAR-22-11-01-A",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 42.659715,
            validFrom = Instant.parse("2022-11-01T00:00:00Z"),
            validTo = Instant.parse("2023-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
    )

    val var221101Rate = listOf(
        Rate(
            vatInclusivePrice = 47.8485,
            validity = Instant.parse("2024-03-31T23:00:00Z")..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 53.3295,
            validity = Instant.parse("2024-03-31T23:00:00Z")..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 42.0105,
            validity = Instant.parse("2024-01-01T00:00:00Z")..Instant.parse("2024-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 47.4915,
            validity = Instant.parse("2024-01-01T00:00:00Z")..Instant.parse("2024-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 42.013125,
            validity = Instant.parse("2023-03-31T23:00:00Z")..Instant.parse("2024-01-01T00:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 47.492655,
            validity = Instant.parse("2023-03-31T23:00:00Z")..Instant.parse("2024-01-01T00:00:00Z"),
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 37.29243,
            validity = Instant.parse("2022-11-01T00:00:00Z")..Instant.parse("2023-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 42.659715,
            validity = Instant.parse("2022-11-01T00:00:00Z")..Instant.parse("2023-03-31T23:00:00Z"),
            paymentMethod = PaymentMethod.NON_DIRECT_DEBIT,
        ),
    )

    val oeFix12M240628Json = """{
    "count": 1,
    "next": null,
    "previous": null,
    "results": [
        {
            "value_exc_vat": 52.22,
            "value_inc_vat": 54.831,
            "valid_from": "2024-06-27T23:00:00Z",
            "valid_to": null,
            "payment_method": null
        }
    ]
}
    """.trimIndent()

    val oeFix12M240628Dto = PricesApiResponse(
        count = 8,
        next = null,
        previous = null,
        results = listOf(
            RateDto(
                vatExclusivePrice = 52.22,
                vatInclusivePrice = 54.831,
                validFrom = Instant.parse("2024-06-27T23:00:00Z"),
                validTo = null,
                paymentMethod = null,
            ),
        ),
    )

    val oeFix12M240628RateEntity = listOf(
        RateEntity(
            tariffCode = "E-1R-OE-FIX-12M-24-06-28-J",
            rateType = RateType.STANDING_CHARGE,
            vatRate = 54.831,
            validFrom = Instant.parse("2024-06-27T23:00:00Z"),
            validTo = null,
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
    )

    val oeFix12M240628Rate = listOf(
        Rate(
            vatInclusivePrice = 54.831,
            validity = Instant.parse("2024-06-27T23:00:00Z")..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.UNKNOWN,
        ),
    )
}
