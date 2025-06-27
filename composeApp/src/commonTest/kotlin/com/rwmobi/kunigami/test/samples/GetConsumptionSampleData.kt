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
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import kotlin.time.Instant

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

    val consumptionWithCost = listOf(
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = 0.113,
                interval = Instant.parse("2024-05-06T23:30:00Z")..Instant.parse("2024-05-07T00:00:00Z"),
            ),
            vatInclusiveCost = 9.4,
            vatInclusiveStandingCharge = 0.06,
        ),
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = 0.58,
                interval = Instant.parse("2024-05-06T23:00:00Z")..Instant.parse("2024-05-06T23:30:00Z"),
            ),
            vatInclusiveCost = 9.5,
            vatInclusiveStandingCharge = 0.06,
        ),
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = 0.201,
                interval = Instant.parse("2024-05-06T22:30:00Z")..Instant.parse("2024-05-06T23:00:00Z"),
            ),
            vatInclusiveCost = 9.6,
            vatInclusiveStandingCharge = 0.06,
        ),
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = 0.451,
                interval = Instant.parse("2024-05-06T22:00:00Z")..Instant.parse("2024-05-06T22:30:00Z"),
            ),
            vatInclusiveCost = 9.7,
            vatInclusiveStandingCharge = 0.06,
        ),
        ConsumptionWithCost(
            consumption = Consumption(
                kWhConsumed = 0.512,
                interval = Instant.parse("2024-05-06T21:30:00Z")..Instant.parse("2024-05-06T22:00:00Z"),
            ),
            vatInclusiveCost = 9.8,
            vatInclusiveStandingCharge = 0.06,
        ),
    )

    val getMeasurementsQueryResponse = """{
  "data": {
    "account": {
      "properties": [
        {
          "id": "1234567",
          "measurements": {
            "edges": [
              {
                "node": {
                  "value": "0.113",
                  "unit": "kwh",
                  "startAt": "2024-05-07T00:30:00+01:00",
                  "endAt": "2024-05-07T01:00:00+01:00",
                  "__typename": "IntervalMeasurementType",
                  "metaData": {
                    "statistics": [
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "0.06",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "STANDING_CHARGE_COST",
                        "__typename": "StatisticOutput"
                      },
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "9.40",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "CONSUMPTION_COST",
                        "__typename": "StatisticOutput"
                      }
                    ],
                    "__typename": "MeasurementsMetadataOutput"
                  }
                },
                "__typename": "MeasurementEdge"
              },
              {
                "node": {
                  "value": "0.58",
                  "unit": "kwh",
                  "startAt": "2024-05-07T00:00:00+01:00",
                  "endAt": "2024-05-07T00:30:00+01:00",
                  "__typename": "IntervalMeasurementType",
                  "metaData": {
                    "statistics": [
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "0.06",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "STANDING_CHARGE_COST",
                        "__typename": "StatisticOutput"
                      },
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "9.50",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "CONSUMPTION_COST",
                        "__typename": "StatisticOutput"
                      }
                    ],
                    "__typename": "MeasurementsMetadataOutput"
                  }
                },
                "__typename": "MeasurementEdge"
              },              
              {
                "node": {
                  "value": "0.201",
                  "unit": "kwh",
                  "startAt": "2024-05-06T23:30:00+01:00",
                  "endAt": "2024-05-07T00:00:00+01:00",
                  "__typename": "IntervalMeasurementType",
                  "metaData": {
                    "statistics": [
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "0.06",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "STANDING_CHARGE_COST",
                        "__typename": "StatisticOutput"
                      },
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "9.60",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "CONSUMPTION_COST",
                        "__typename": "StatisticOutput"
                      }
                    ],
                    "__typename": "MeasurementsMetadataOutput"
                  }
                },
                "__typename": "MeasurementEdge"
              },
              {
                "node": {
                  "value": "0.451",
                  "unit": "kwh",
                  "startAt": "2024-05-06T23:00:00+01:00",
                  "endAt": "2024-05-06T23:30:00+01:00",
                  "__typename": "IntervalMeasurementType",
                  "metaData": {
                    "statistics": [
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "0.06",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "STANDING_CHARGE_COST",
                        "__typename": "StatisticOutput"
                      },
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "9.70",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "CONSUMPTION_COST",
                        "__typename": "StatisticOutput"
                      }
                    ],
                    "__typename": "MeasurementsMetadataOutput"
                  }
                },
                "__typename": "MeasurementEdge"
              },
              {
                "node": {
                  "value": "0.512",
                  "unit": "kwh",
                  "startAt": "2024-05-06T22:30:00+01:00",
                  "endAt": "2024-05-06T23:00:00+01:00",
                  "__typename": "IntervalMeasurementType",
                  "metaData": {
                    "statistics": [
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "0.06",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "STANDING_CHARGE_COST",
                        "__typename": "StatisticOutput"
                      },
                      {
                        "costInclTax": {
                          "costCurrency": "GBP",
                          "estimatedAmount": "9.80",
                          "__typename": "EstimatedMoneyType"
                        },
                        "type": "CONSUMPTION_COST",
                        "__typename": "StatisticOutput"
                      }
                    ],
                    "__typename": "MeasurementsMetadataOutput"
                  }
                },
                "__typename": "MeasurementEdge"
              }
            ],
            "pageInfo": {
              "hasNextPage": false,
              "endCursor": "end-cursor",
              "__typename": "PageInfo"
            },
            "__typename": "MeasurementConnection"
          },
          "__typename": "PropertyType"
        }
      ],
      "__typename": "AccountType"
    }
  }
}
    """.trimIndent()
}
