/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.data.source.network.dto.account.AccountApiResponse
import com.rwmobi.kunigami.data.source.network.dto.account.AgreementDto
import com.rwmobi.kunigami.data.source.network.dto.account.ElectricityMeterPointDto
import com.rwmobi.kunigami.data.source.network.dto.account.GasMeterPointDto
import com.rwmobi.kunigami.data.source.network.dto.account.MeterDto
import com.rwmobi.kunigami.data.source.network.dto.account.PropertyDto
import com.rwmobi.kunigami.data.source.network.dto.account.RegisterDto
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import kotlinx.datetime.Instant

object GetAccountSampleData {
    val json = """{
  "number": "B-1234A1A1",
  "properties": [
    {
      "id": 1234567,
      "moved_in_at": "2020-11-30T00:00:00Z",
      "moved_out_at": null,
      "address_line_1": "10 Downing Street",
      "address_line_2": "",
      "address_line_3": "",
      "town": "LONDON",
      "county": "",
      "postcode": "W1 1AA",
      "electricity_meter_points": [
        {
          "mpan": "1000000000000",
          "profile_class": 1,
          "consumption_standard": 2560,
          "meters": [
            {
              "serial_number": "1111111111",
              "registers": [
                {
                  "identifier": "1",
                  "rate": "STANDARD",
                  "is_settlement_register": true
                }
              ]
            },
            {
              "serial_number": "2222222222",
              "registers": [
                {
                  "identifier": "1",
                  "rate": "STANDARD",
                  "is_settlement_register": true
                }
              ]
            }
          ],
          "agreements": [
            {
              "tariff_code": "E-1R-VAR-20-09-22-N",
              "valid_from": "2020-12-17T00:00:00Z",
              "valid_to": "2021-12-17T00:00:00Z"
            },
            {
              "tariff_code": "E-1R-VAR-21-09-29-N",
              "valid_from": "2021-12-17T00:00:00Z",
              "valid_to": "2023-04-01T00:00:00+01:00"
            },
            {
              "tariff_code": "E-1R-VAR-22-11-01-N",
              "valid_from": "2023-04-01T00:00:00+01:00",
              "valid_to": null
            }
          ],
          "is_export": false
        }
      ],
      "gas_meter_points": [
        {
          "mprn": "1234567890",
          "consumption_standard": 3448,
          "meters": [
            {
              "serial_number": "12345678901234"
            },
            {
              "serial_number": "09876543210987"
            }
          ],
          "agreements": [
            {
              "tariff_code": "G-1R-VAR-20-09-22-N",
              "valid_from": "2020-12-17T00:00:00Z",
              "valid_to": "2021-12-17T00:00:00Z"
            },
            {
              "tariff_code": "G-1R-VAR-21-09-29-N",
              "valid_from": "2021-12-17T00:00:00Z",
              "valid_to": "2023-04-01T00:00:00+01:00"
            },
            {
              "tariff_code": "G-1R-VAR-22-11-01-N",
              "valid_from": "2023-04-01T00:00:00+01:00",
              "valid_to": null
            }
          ]
        }
      ]
    }
  ]
}
    """.trimIndent()

    val dto = AccountApiResponse(
        number = "B-1234A1A1",
        properties = listOf(
            PropertyDto(
                id = 1234567, movedInAt = Instant.parse("2020-11-30T00:00:00Z"), movedOutAt = null, addressLine1 = "10 Downing Street", addressLine2 = "", addressLine3 = "", town = "LONDON", county = "", postcode = "W1 1AA",
                electricityMeterPoints = listOf(
                    ElectricityMeterPointDto(
                        mpan = "1000000000000",
                        profileClass = 1,
                        consumptionStandard = 2560,
                        meters = listOf(
                            MeterDto(serialNumber = "1111111111", registers = listOf(RegisterDto(identifier = "1", rate = "STANDARD", isSettlementRegister = true))),
                            MeterDto(serialNumber = "2222222222", registers = listOf(RegisterDto(identifier = "1", rate = "STANDARD", isSettlementRegister = true))),
                        ),
                        agreements = listOf(
                            AgreementDto(tariffCode = "E-1R-VAR-20-09-22-N", validFrom = Instant.parse("2020-12-17T00:00:00Z"), validTo = Instant.parse("2021-12-17T00:00:00Z")),
                            AgreementDto(tariffCode = "E-1R-VAR-21-09-29-N", validFrom = Instant.parse("2021-12-17T00:00:00Z"), validTo = Instant.parse("2023-03-31T23:00:00Z")),
                            AgreementDto(tariffCode = "E-1R-VAR-22-11-01-N", validFrom = Instant.parse("2023-03-31T23:00:00Z"), validTo = null),
                        ),
                        isExport = false,
                    ),
                ),
                gasMeterPoints = listOf(
                    GasMeterPointDto(
                        mprn = "1234567890",
                        consumptionStandard = 3448,
                        meters = listOf(
                            MeterDto(serialNumber = "12345678901234", registers = null),
                            MeterDto(serialNumber = "09876543210987", registers = null),
                        ),
                        agreements = listOf(
                            AgreementDto(tariffCode = "G-1R-VAR-20-09-22-N", validFrom = Instant.parse("2020-12-17T00:00:00Z"), validTo = Instant.parse("2021-12-17T00:00:00Z")),
                            AgreementDto(tariffCode = "G-1R-VAR-21-09-29-N", validFrom = Instant.parse("2021-12-17T00:00:00Z"), validTo = Instant.parse("2023-03-31T23:00:00Z")),
                            AgreementDto(tariffCode = "G-1R-VAR-22-11-01-N", validFrom = Instant.parse("2023-03-31T23:00:00Z"), validTo = null),
                        ),
                    ),
                ),
            ),
        ),
    )

    val emptyPropertiesQueryResponse = """{
  "data": {
    "properties": []
  }
}
    """.trimIndent()

    val propertiesQueryResponse = """{
  "data": {
    "properties": [
      {
        "address": "10 Downing Street, LONDON, W1 1AA",
        "postcode": "W1 1AA",
        "occupancyPeriods": [
          {
            "effectiveFrom": "2020-11-30T00:00:00Z",
            "effectiveTo": null
          }
        ],
        "electricityMeterPoints": [
          {
            "mpan": "1000000000000",
            "meters": [
              {
                "serialNumber": "1111111111",
                "makeAndType": "",
                "meterPoint": {
                  "meters": [
                    {
                      "serialNumber": "1111111111",
                      "readings": {
                        "edges": [
                          {
                            "node": {
                              "readAt": "",
                              "source": "",
                              "readingSource": "",
                              "registers": [
                                {
                                  "value": ""
                                }
                              ]
                            }
                          }
                        ]
                      }
                    }
                  ]
                }
              },
              {
                "serialNumber": "2222222222",
                "makeAndType": "",
                "meterPoint": {
                  "meters": [
                    {
                      "serialNumber": "2222222222",
                      "readings": {
                        "edges": [
                          {
                            "node": {
                              "readAt": "",
                              "source": "",
                              "readingSource": "",
                              "registers": [
                                {
                                  "value": ""
                                }
                              ]
                            }
                          }
                        ]
                      }
                    }
                  ]
                }
              }
            ],
            "agreements": [
              {
                "validFrom": "2021-12-17T00:00:00Z",
                "validTo": "2023-04-01T00:00:00+01:00",
                "tariff": {
                  "__typename": "StandardTariff",
                  "displayName": "Flexible Octopus October 2021 v2",
                  "fullName": "Flexible Octopus October 2021 v2",
                  "description": "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change, and you can switch to a fixed tariff at any time.",
                  "standingCharge": null,
                  "tariffCode": "E-1R-VAR-21-09-29-C",
                  "unitRate": null
                }
              },
              {
                "validFrom": "2023-04-01T00:00:00+01:00",
                "validTo": null,
                "tariff": {
                  "__typename": "StandardTariff",
                  "displayName": "Flexible Octopus",
                  "fullName": "Flexible Octopus",
                  "description": "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
                  "standingCharge": 38.72,
                  "tariffCode": "E-1R-VAR-22-11-01-C",
                  "unitRate": 23.53
                }
              }
            ]
          }
        ]
      }
    ]
  }
}
    """.trimIndent()

    val account = Account(
        accountNumber = "B-1234A1A1",
        fullAddress = "10 Downing Street, LONDON, W1 1AA",
        postcode = "W1 1AA",
        movedInAt = Instant.parse("2020-11-30T00:00:00Z"),
        movedOutAt = null,
        electricityMeterPoints = listOf(
            ElectricityMeterPoint(
                mpan = "1000000000000",
                meterSerialNumbers = listOf("1111111111", "2222222222"),
                agreements = listOf(
                    Agreement(
                        tariffCode = "E-1R-VAR-22-11-01-C",
                        period = Instant.parse("2023-03-31T23:00:00Z")..Instant.parse("+100000-01-01T00:00:00Z"),
                        fullName = "Flexible Octopus",
                        displayName = "Flexible Octopus",
                        description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
                        isHalfHourlyTariff = false,
                        vatInclusiveStandingCharge = 38.72,
                        vatInclusiveStandardUnitRate = 23.53,
                        vatInclusiveDayUnitRate = null,
                        vatInclusiveNightUnitRate = null,
                        vatInclusiveOffPeakRate = null,
                        agilePriceCap = null,
                    ),
                ),
            ),
        ),
    )
}
