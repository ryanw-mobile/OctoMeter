/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.samples

import com.rwmobi.kunigami.data.source.network.dto.account.AccountApiResponse
import com.rwmobi.kunigami.data.source.network.dto.account.AgreementDto
import com.rwmobi.kunigami.data.source.network.dto.account.ElectricityMeterPointDto
import com.rwmobi.kunigami.data.source.network.dto.account.GasMeterPointDto
import com.rwmobi.kunigami.data.source.network.dto.account.MeterDto
import com.rwmobi.kunigami.data.source.network.dto.account.PropertyDto
import com.rwmobi.kunigami.data.source.network.dto.account.RegisterDto
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
}
