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

import com.rwmobi.kunigami.data.source.network.dto.LinkDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.ConsumptionDetailDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.DualFuelConsumptionDetailDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.DualRateConsumptionDetailDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.ElectricityRateQuotesDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.ElectricityTariffDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.QuotesDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SampleConsumptionDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SampleQuotesDto
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.SingleProductApiResponse
import com.rwmobi.kunigami.data.source.network.dto.singleproduct.TariffDetailsDto
import kotlin.time.Instant

object GetProductSampleData {

    val json_var_22_11_01 = """{
  "code": "VAR-22-11-01",
  "full_name": "Flexible Octopus",
  "display_name": "Flexible Octopus",
  "description": "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
  "is_variable": true,
  "is_green": false,
  "is_tracker": false,
  "is_prepay": false,
  "is_business": false,
  "is_restricted": false,
  "term": null,
  "available_from": "2023-03-28T10:35:00+01:00",
  "available_to": null,
  "tariffs_active_at": "2024-05-13T17:13:39.477498Z",
  "single_register_electricity_tariffs": {
    "_A": {
      "varying": {
        "code": "E-1R-VAR-22-11-01-A",
        "standing_charge_exc_vat": 45.57,
        "standing_charge_inc_vat": 47.8485,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-A/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 24.052,
        "standard_unit_rate_inc_vat": 25.2546
      }
    },
    "_B": {
      "varying": {
        "code": "E-1R-VAR-22-11-01-B",
        "standing_charge_exc_vat": 51.37,
        "standing_charge_inc_vat": 53.9385,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-B/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 22.64,
        "standard_unit_rate_inc_vat": 23.772
      }
    },
    "_C": {
      "varying": {
        "code": "E-1R-VAR-22-11-01-C",
        "standing_charge_exc_vat": 36.88,
        "standing_charge_inc_vat": 38.724,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-C/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 24.496,
        "standard_unit_rate_inc_vat": 25.7208
      }
    }
  },
  "dual_register_electricity_tariffs": {
    "_A": {
      "varying": {
        "code": "E-2R-VAR-22-11-01-A",
        "standing_charge_exc_vat": 46.09,
        "standing_charge_inc_vat": 48.3945,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-A/day-unit-rates/",
            "method": "GET",
            "rel": "day_unit_rates"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-A/night-unit-rates/",
            "method": "GET",
            "rel": "night_unit_rates"
          }
        ],
        "day_unit_rate_exc_vat": 30.008,
        "day_unit_rate_inc_vat": 31.5084,
        "night_unit_rate_exc_vat": 12.588,
        "night_unit_rate_inc_vat": 13.2174
      }
    },
    "_B": {
      "varying": {
        "code": "E-2R-VAR-22-11-01-B",
        "standing_charge_exc_vat": 51.1,
        "standing_charge_inc_vat": 53.655,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-B/day-unit-rates/",
            "method": "GET",
            "rel": "day_unit_rates"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-B/night-unit-rates/",
            "method": "GET",
            "rel": "night_unit_rates"
          }
        ],
        "day_unit_rate_exc_vat": 28.046,
        "day_unit_rate_inc_vat": 29.4483,
        "night_unit_rate_exc_vat": 12.503,
        "night_unit_rate_inc_vat": 13.12815
      }
    },
    "_C": {
      "varying": {
        "code": "E-2R-VAR-22-11-01-C",
        "standing_charge_exc_vat": 36.84,
        "standing_charge_inc_vat": 38.682,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-C/day-unit-rates/",
            "method": "GET",
            "rel": "day_unit_rates"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-C/night-unit-rates/",
            "method": "GET",
            "rel": "night_unit_rates"
          }
        ],
        "day_unit_rate_exc_vat": 30.451,
        "day_unit_rate_inc_vat": 31.97355,
        "night_unit_rate_exc_vat": 12.866,
        "night_unit_rate_inc_vat": 13.5093
      }
    }
  },
  "single_register_gas_tariffs": {
    "_A": {
      "varying": {
        "code": "G-1R-VAR-22-11-01-A",
        "standing_charge_exc_vat": 27.57,
        "standing_charge_inc_vat": 28.9485,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/gas-tariffs/G-1R-VAR-22-11-01-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/gas-tariffs/G-1R-VAR-22-11-01-A/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 5.679,
        "standard_unit_rate_inc_vat": 5.96295
      }
    },
    "_B": {
      "varying": {
        "code": "G-1R-VAR-22-11-01-B",
        "standing_charge_exc_vat": 27.66,
        "standing_charge_inc_vat": 29.043,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/gas-tariffs/G-1R-VAR-22-11-01-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/gas-tariffs/G-1R-VAR-22-11-01-B/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 5.617,
        "standard_unit_rate_inc_vat": 5.89785
      }
    },
    "_C": {
      "varying": {
        "code": "G-1R-VAR-22-11-01-C",
        "standing_charge_exc_vat": 28.55,
        "standing_charge_inc_vat": 29.9775,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/gas-tariffs/G-1R-VAR-22-11-01-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/gas-tariffs/G-1R-VAR-22-11-01-C/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 5.82,
        "standard_unit_rate_inc_vat": 6.111
      }
    }
  },
  "sample_quotes": {
    "_A": {
      "varying": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 85652,
          "annual_cost_exc_vat": 81573
        },
        "electricity_dual_rate": {
          "annual_cost_inc_vat": 110586,
          "annual_cost_exc_vat": 105320
        },
        "dual_fuel_single_rate": {
          "annual_cost_inc_vat": 164792,
          "annual_cost_exc_vat": 156945
        },
        "dual_fuel_dual_rate": {
          "annual_cost_inc_vat": 189726,
          "annual_cost_exc_vat": 180692
        }
      }
    },
    "_B": {
      "varying": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 83872,
          "annual_cost_exc_vat": 79878
        },
        "electricity_dual_rate": {
          "annual_cost_inc_vat": 107700,
          "annual_cost_exc_vat": 102571
        },
        "dual_fuel_single_rate": {
          "annual_cost_inc_vat": 162298,
          "annual_cost_exc_vat": 154569
        },
        "dual_fuel_dual_rate": {
          "annual_cost_inc_vat": 186126,
          "annual_cost_exc_vat": 177263
        }
      }
    },
    "_C": {
      "varying": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 83580,
          "annual_cost_exc_vat": 79600
        },
        "electricity_dual_rate": {
          "annual_cost_inc_vat": 108571,
          "annual_cost_exc_vat": 103401
        },
        "dual_fuel_single_rate": {
          "annual_cost_inc_vat": 164799,
          "annual_cost_exc_vat": 156951
        },
        "dual_fuel_dual_rate": {
          "annual_cost_inc_vat": 189790,
          "annual_cost_exc_vat": 180752
        }
      }
    }
  },
  "sample_consumption": {
    "electricity_single_rate": {
      "electricity_standard": 2700
    },
    "electricity_dual_rate": {
      "electricity_day": 2262,
      "electricity_night": 1638
    },
    "dual_fuel_single_rate": {
      "electricity_standard": 2700,
      "gas_standard": 11500
    },
    "dual_fuel_dual_rate": {
      "electricity_day": 2262,
      "electricity_night": 1638,
      "gas_standard": 11500
    }
  },
  "links": [
    {
      "href": "https://api.octopus.energy/v1/products/VAR-22-11-01/",
      "method": "GET",
      "rel": "self"
    }
  ],
  "brand": "OCTOPUS_ENERGY"
}
    """.trimIndent()

    val dto_var_22_11_01 = SingleProductApiResponse(
        code = "VAR-22-11-01",
        fullName = "Flexible Octopus",
        displayName = "Flexible Octopus",
        description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
        isVariable = true,
        isGreen = false,
        isTracker = false,
        isPrepay = false,
        isBusiness = false,
        isRestricted = false,
        term = null,
        availableFrom = Instant.parse("2023-03-28T09:35:00Z"),
        availableTo = null,
        tariffsActiveAt = Instant.parse("2024-05-13T17:13:39.477498Z"),
        singleRegisterElectricityTariffs = mapOf(
            "_A" to TariffDetailsDto(
                directDebitMonthly = null,
                varying = ElectricityTariffDto(
                    code = "E-1R-VAR-22-11-01-A",
                    standingChargeExcVat = 45.57,
                    standingChargeIncVat = 47.8485,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 24.052,
                    standardUnitRateIncVat = 25.2546,
                    dayUnitRateExcVat = null,
                    dayUnitRateIncVat = null,
                    nightUnitRateExcVat = null,
                    nightUnitRateIncVat = null,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-A/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-A/standard-unit-rates/",
                            method = "GET",
                            rel = "standard_unit_rates",
                        ),
                    ),
                ),
            ),
            "_B" to TariffDetailsDto(
                directDebitMonthly = null,
                varying = ElectricityTariffDto(
                    code = "E-1R-VAR-22-11-01-B",
                    standingChargeExcVat = 51.37,
                    standingChargeIncVat = 53.9385,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 22.64,
                    standardUnitRateIncVat = 23.772,
                    dayUnitRateExcVat = null,
                    dayUnitRateIncVat = null,
                    nightUnitRateExcVat = null,
                    nightUnitRateIncVat = null,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-B/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-B/standard-unit-rates/",
                            method = "GET",
                            rel = "standard_unit_rates",
                        ),
                    ),
                ),
            ),
            "_C" to TariffDetailsDto(
                directDebitMonthly = null,
                varying = ElectricityTariffDto(
                    code = "E-1R-VAR-22-11-01-C",
                    standingChargeExcVat = 36.88,
                    standingChargeIncVat = 38.724,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 24.496,
                    standardUnitRateIncVat = 25.7208,
                    dayUnitRateExcVat = null,
                    dayUnitRateIncVat = null,
                    nightUnitRateExcVat = null,
                    nightUnitRateIncVat = null,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-C/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-1R-VAR-22-11-01-C/standard-unit-rates/",
                            method = "GET",
                            rel = "standard_unit_rates",
                        ),
                    ),
                ),
            ),
        ),
        dualRegisterElectricityTariffs = mapOf(
            "_A" to TariffDetailsDto(
                directDebitMonthly = null,
                varying = ElectricityTariffDto(
                    code = "E-2R-VAR-22-11-01-A",
                    standingChargeExcVat = 46.09,
                    standingChargeIncVat = 48.3945,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = null,
                    standardUnitRateIncVat = null,
                    dayUnitRateExcVat = 30.008,
                    dayUnitRateIncVat = 31.5084,
                    nightUnitRateExcVat = 12.588,
                    nightUnitRateIncVat = 13.2174,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-A/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-A/day-unit-rates/",
                            method = "GET",
                            rel = "day_unit_rates",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-A/night-unit-rates/",
                            method = "GET",
                            rel = "night_unit_rates",
                        ),
                    ),
                ),
            ),
            "_B" to TariffDetailsDto(
                directDebitMonthly = null,
                varying = ElectricityTariffDto(
                    code = "E-2R-VAR-22-11-01-B",
                    standingChargeExcVat = 51.1,
                    standingChargeIncVat = 53.655,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = null,
                    standardUnitRateIncVat = null,
                    dayUnitRateExcVat = 28.046,
                    dayUnitRateIncVat = 29.4483,
                    nightUnitRateExcVat = 12.503,
                    nightUnitRateIncVat = 13.12815,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-B/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-B/day-unit-rates/",
                            method = "GET",
                            rel = "day_unit_rates",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-B/night-unit-rates/",
                            method = "GET",
                            rel = "night_unit_rates",
                        ),
                    ),
                ),
            ),
            "_C" to TariffDetailsDto(
                directDebitMonthly = null,
                varying = ElectricityTariffDto(
                    code = "E-2R-VAR-22-11-01-C",
                    standingChargeExcVat = 36.84,
                    standingChargeIncVat = 38.682,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = null,
                    standardUnitRateIncVat = null,
                    dayUnitRateExcVat = 30.451,
                    dayUnitRateIncVat = 31.97355,
                    nightUnitRateExcVat = 12.866,
                    nightUnitRateIncVat = 13.5093,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-C/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-C/day-unit-rates/",
                            method = "GET",
                            rel = "day_unit_rates",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/VAR-22-11-01/electricity-tariffs/E-2R-VAR-22-11-01-C/night-unit-rates/",
                            method = "GET",
                            rel = "night_unit_rates",
                        ),
                    ),
                ),
            ),
        ),
        brand = "OCTOPUS_ENERGY",
        links = listOf(
            LinkDto(href = "https://api.octopus.energy/v1/products/VAR-22-11-01/", method = "GET", rel = "self"),
        ),
        sampleQuotes = mapOf(
            "_A" to SampleQuotesDto(
                directDebitMonthly = null,
                varying = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 85652,
                        annualCostExcVat = 81573,
                    ),
                ),
            ),
            "_B" to SampleQuotesDto(
                directDebitMonthly = null,
                varying = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 83872,
                        annualCostExcVat = 79878,
                    ),
                ),
            ),
            "_C" to SampleQuotesDto(
                directDebitMonthly = null,
                varying = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 83580,
                        annualCostExcVat = 79600,
                    ),
                ),
            ),
        ),
        sampleConsumption = SampleConsumptionDto(
            electricitySingleRate = ConsumptionDetailDto(
                electricityStandard = 2700,
            ),
            electricityDualRate = DualRateConsumptionDetailDto(
                electricityDay = 2262,
                electricityNight = 1638,
                gasStandard = null,
            ),
            dualFuelSingleRate = DualFuelConsumptionDetailDto(
                electricityStandard = 2700,
                gasStandard = 11500,
            ),
            dualFuelDualRate = DualRateConsumptionDetailDto(
                electricityDay = 2262,
                electricityNight = 1638,
                gasStandard = 11500,
            ),
        ),
    )

    val json_agile_flex_22_11_25 = """{
  "code": "AGILE-FLEX-22-11-25",
  "full_name": "Agile Octopus November 2022 v1",
  "display_name": "Agile Octopus",
  "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
  "is_variable": true,
  "is_green": true,
  "is_tracker": false,
  "is_prepay": false,
  "is_business": false,
  "is_restricted": false,
  "term": null,
  "available_from": "2022-11-25T00:00:00Z",
  "available_to": "2023-12-11T12:00:00Z",
  "tariffs_active_at": "2024-05-13T05:30:22.560405Z",
  "single_register_electricity_tariffs": {
    "_A": {
      "direct_debit_monthly": {
        "code": "E-1R-AGILE-FLEX-22-11-25-A",
        "standing_charge_exc_vat": 35.52,
        "standing_charge_inc_vat": 37.296,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-A/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 16,
        "standard_unit_rate_inc_vat": 16.8
      }
    },
    "_B": {
      "direct_debit_monthly": {
        "code": "E-1R-AGILE-FLEX-22-11-25-B",
        "standing_charge_exc_vat": 42.02,
        "standing_charge_inc_vat": 44.121,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-B/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 15.24,
        "standard_unit_rate_inc_vat": 16.002
      }
    },
    "_C": {
      "direct_debit_monthly": {
        "code": "E-1R-AGILE-FLEX-22-11-25-C",
        "standing_charge_exc_vat": 30.01,
        "standing_charge_inc_vat": 31.5105,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-C/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 15.24,
        "standard_unit_rate_inc_vat": 16.002
      }
    }
  },
  "dual_register_electricity_tariffs": {},
  "single_register_gas_tariffs": {},
  "sample_quotes": {
    "_A": {
      "direct_debit_monthly": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 58973,
          "annual_cost_exc_vat": 56165
        }
      }
    },
    "_B": {
      "direct_debit_monthly": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 59310,
          "annual_cost_exc_vat": 56485
        }
      }
    },
    "_C": {
      "direct_debit_monthly": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 54707,
          "annual_cost_exc_vat": 52102
        }
      }
    },
  },
  "sample_consumption": {
    "electricity_single_rate": {
      "electricity_standard": 2700
    },
    "electricity_dual_rate": {
      "electricity_day": 2262,
      "electricity_night": 1638
    },
    "dual_fuel_single_rate": {
      "electricity_standard": 2700,
      "gas_standard": 11500
    },
    "dual_fuel_dual_rate": {
      "electricity_day": 2262,
      "electricity_night": 1638,
      "gas_standard": 11500
    }
  },
  "links": [
    {
      "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/",
      "method": "GET",
      "rel": "self"
    }
  ],
  "brand": "OCTOPUS_ENERGY"
}
    """.trimIndent()

    val dto_agile_flex_22_11_25 = SingleProductApiResponse(
        code = "AGILE-FLEX-22-11-25",
        fullName = "Agile Octopus November 2022 v1",
        displayName = "Agile Octopus",
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        isVariable = true,
        isGreen = true,
        isTracker = false,
        isPrepay = false,
        isBusiness = false,
        isRestricted = false,
        term = null,
        availableFrom = Instant.parse("2022-11-25T00:00:00Z"),
        availableTo = Instant.parse("2023-12-11T12:00:00Z"),
        tariffsActiveAt = Instant.parse("2024-05-13T05:30:22.560405Z"),
        singleRegisterElectricityTariffs = mapOf(
            "_A" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-1R-AGILE-FLEX-22-11-25-A",
                    standingChargeExcVat = 35.52,
                    standingChargeIncVat = 37.296,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 16.0,
                    standardUnitRateIncVat = 16.8,
                    links = listOf(
                        LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-A/standing-charges/", method = "GET", rel = "standing_charges"),
                        LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-A/standard-unit-rates/", method = "GET", rel = "standard_unit_rates"),
                    ),
                ),
                varying = null,
            ),
            "_B" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-1R-AGILE-FLEX-22-11-25-B",
                    standingChargeExcVat = 42.02,
                    standingChargeIncVat = 44.121,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 15.24,
                    standardUnitRateIncVat = 16.002,
                    links = listOf(
                        LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-B/standing-charges/", method = "GET", rel = "standing_charges"),
                        LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-B/standard-unit-rates/", method = "GET", rel = "standard_unit_rates"),
                    ),
                ),
                varying = null,
            ),
            "_C" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-1R-AGILE-FLEX-22-11-25-C",
                    standingChargeExcVat = 30.01,
                    standingChargeIncVat = 31.5105,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 15.24,
                    standardUnitRateIncVat = 16.002,
                    links = listOf(
                        LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-C/standing-charges/", method = "GET", rel = "standing_charges"),
                        LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-C/standard-unit-rates/", method = "GET", rel = "standard_unit_rates"),
                    ),
                ),
                varying = null,
            ),
        ),
        dualRegisterElectricityTariffs = emptyMap(),
        brand = "OCTOPUS_ENERGY",
        links = listOf(
            LinkDto(href = "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/", method = "GET", rel = "self"),
        ),
        sampleQuotes = mapOf(
            "_A" to SampleQuotesDto(
                directDebitMonthly = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 58973,
                        annualCostExcVat = 56165,
                    ),
                ),
                varying = null,
            ),
            "_B" to SampleQuotesDto(
                directDebitMonthly = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 59310,
                        annualCostExcVat = 56485,
                    ),
                ),
                varying = null,
            ),
            "_C" to SampleQuotesDto(
                directDebitMonthly = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 54707,
                        annualCostExcVat = 52102,
                    ),
                ),
                varying = null,
            ),
        ),
        sampleConsumption = SampleConsumptionDto(
            electricitySingleRate = ConsumptionDetailDto(
                electricityStandard = 2700,
            ),
            electricityDualRate = DualRateConsumptionDetailDto(
                electricityDay = 2262,
                electricityNight = 1638,
                gasStandard = null,
            ),
            dualFuelSingleRate = DualFuelConsumptionDetailDto(
                electricityStandard = 2700,
                gasStandard = 11500,
            ),
            dualFuelDualRate = DualRateConsumptionDetailDto(
                electricityDay = 2262,
                electricityNight = 1638,
                gasStandard = 11500,
            ),
        ),
    )

    val json_oe_fix_12m_24_04_11 = """{
  "code": "OE-FIX-12M-24-04-11",
  "full_name": "Octopus 12M Fixed April 2024 v1",
  "display_name": "Octopus 12M Fixed",
  "description": "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
  "is_variable": false,
  "is_green": false,
  "is_tracker": false,
  "is_prepay": false,
  "is_business": false,
  "is_restricted": false,
  "term": 12,
  "available_from": "2024-04-11T00:00:00+01:00",
  "available_to": "2024-05-04T00:00:00+01:00",
  "tariffs_active_at": "2024-05-13T16:00:13.822567Z",
  "single_register_electricity_tariffs": {
    "_A": {
      "direct_debit_monthly": {
        "code": "E-1R-OE-FIX-12M-24-04-11-A",
        "standing_charge_exc_vat": 45.57,
        "standing_charge_inc_vat": 47.8485,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-A/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 23.344,
        "standard_unit_rate_inc_vat": 24.5112
      }
    },
    "_B": {
      "direct_debit_monthly": {
        "code": "E-1R-OE-FIX-12M-24-04-11-B",
        "standing_charge_exc_vat": 51.37,
        "standing_charge_inc_vat": 53.9385,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-B/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 21.932,
        "standard_unit_rate_inc_vat": 23.0286
      }
    },
    "_C": {
      "direct_debit_monthly": {
        "code": "E-1R-OE-FIX-12M-24-04-11-C",
        "standing_charge_exc_vat": 36.88,
        "standing_charge_inc_vat": 38.724,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-C/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 23.788,
        "standard_unit_rate_inc_vat": 24.9774
      }
    }
  },
  "dual_register_electricity_tariffs": {
    "_A": {
      "direct_debit_monthly": {
        "code": "E-2R-OE-FIX-12M-24-04-11-A",
        "standing_charge_exc_vat": 46.09,
        "standing_charge_inc_vat": 48.3945,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-A/day-unit-rates/",
            "method": "GET",
            "rel": "day_unit_rates"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-A/night-unit-rates/",
            "method": "GET",
            "rel": "night_unit_rates"
          }
        ],
        "day_unit_rate_exc_vat": 29.1791,
        "day_unit_rate_inc_vat": 30.638055,
        "night_unit_rate_exc_vat": 12.2402,
        "night_unit_rate_inc_vat": 12.85221
      }
    },
    "_B": {
      "direct_debit_monthly": {
        "code": "E-2R-OE-FIX-12M-24-04-11-B",
        "standing_charge_exc_vat": 51.1,
        "standing_charge_inc_vat": 53.655,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-B/day-unit-rates/",
            "method": "GET",
            "rel": "day_unit_rates"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-B/night-unit-rates/",
            "method": "GET",
            "rel": "night_unit_rates"
          }
        ],
        "day_unit_rate_exc_vat": 27.2291,
        "day_unit_rate_inc_vat": 28.590555,
        "night_unit_rate_exc_vat": 12.1388,
        "night_unit_rate_inc_vat": 12.74574
      }
    },
    "_C": {
      "direct_debit_monthly": {
        "code": "E-2R-OE-FIX-12M-24-04-11-C",
        "standing_charge_exc_vat": 36.84,
        "standing_charge_inc_vat": 38.682,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-C/day-unit-rates/",
            "method": "GET",
            "rel": "day_unit_rates"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-C/night-unit-rates/",
            "method": "GET",
            "rel": "night_unit_rates"
          }
        ],
        "day_unit_rate_exc_vat": 29.6235,
        "day_unit_rate_inc_vat": 31.104675,
        "night_unit_rate_exc_vat": 12.5164,
        "night_unit_rate_inc_vat": 13.14222
      }
    }
  },
  "single_register_gas_tariffs": {
    "_A": {
      "direct_debit_monthly": {
        "code": "G-1R-OE-FIX-12M-24-04-11-A",
        "standing_charge_exc_vat": 27.57,
        "standing_charge_inc_vat": 28.9485,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/gas-tariffs/G-1R-OE-FIX-12M-24-04-11-A/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/gas-tariffs/G-1R-OE-FIX-12M-24-04-11-A/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 5.3059,
        "standard_unit_rate_inc_vat": 5.571195
      }
    },
    "_B": {
      "direct_debit_monthly": {
        "code": "G-1R-OE-FIX-12M-24-04-11-B",
        "standing_charge_exc_vat": 27.66,
        "standing_charge_inc_vat": 29.043,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/gas-tariffs/G-1R-OE-FIX-12M-24-04-11-B/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/gas-tariffs/G-1R-OE-FIX-12M-24-04-11-B/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 5.2439,
        "standard_unit_rate_inc_vat": 5.506095
      }
    },
    "_C": {
      "direct_debit_monthly": {
        "code": "G-1R-OE-FIX-12M-24-04-11-C",
        "standing_charge_exc_vat": 28.55,
        "standing_charge_inc_vat": 29.9775,
        "online_discount_exc_vat": 0,
        "online_discount_inc_vat": 0,
        "dual_fuel_discount_exc_vat": 0,
        "dual_fuel_discount_inc_vat": 0,
        "exit_fees_exc_vat": 0,
        "exit_fees_inc_vat": 0,
        "exit_fees_type": "NONE",
        "links": [
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/gas-tariffs/G-1R-OE-FIX-12M-24-04-11-C/standing-charges/",
            "method": "GET",
            "rel": "standing_charges"
          },
          {
            "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/gas-tariffs/G-1R-OE-FIX-12M-24-04-11-C/standard-unit-rates/",
            "method": "GET",
            "rel": "standard_unit_rates"
          }
        ],
        "standard_unit_rate_exc_vat": 5.4469,
        "standard_unit_rate_inc_vat": 5.719245
      }
    }
  },
  "sample_quotes": {
    "_A": {
      "direct_debit_monthly": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 83645,
          "annual_cost_exc_vat": 79662
        },
        "electricity_dual_rate": {
          "annual_cost_inc_vat": 108019,
          "annual_cost_exc_vat": 102875
        },
        "dual_fuel_single_rate": {
          "annual_cost_inc_vat": 158280,
          "annual_cost_exc_vat": 150743
        },
        "dual_fuel_dual_rate": {
          "annual_cost_inc_vat": 182654,
          "annual_cost_exc_vat": 173956
        }
      }
    },
    "_B": {
      "direct_debit_monthly": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 81865,
          "annual_cost_exc_vat": 77966
        },
        "electricity_dual_rate": {
          "annual_cost_inc_vat": 105133,
          "annual_cost_exc_vat": 100127
        },
        "dual_fuel_single_rate": {
          "annual_cost_inc_vat": 155786,
          "annual_cost_exc_vat": 148367
        },
        "dual_fuel_dual_rate": {
          "annual_cost_inc_vat": 179054,
          "annual_cost_exc_vat": 170528
        }
      }
    },
    "_C": {
      "direct_debit_monthly": {
        "electricity_single_rate": {
          "annual_cost_inc_vat": 81573,
          "annual_cost_exc_vat": 77689
        },
        "electricity_dual_rate": {
          "annual_cost_inc_vat": 106005,
          "annual_cost_exc_vat": 100957
        },
        "dual_fuel_single_rate": {
          "annual_cost_inc_vat": 158286,
          "annual_cost_exc_vat": 150749
        },
        "dual_fuel_dual_rate": {
          "annual_cost_inc_vat": 182718,
          "annual_cost_exc_vat": 174017
        }
      }
    }
  },
  "sample_consumption": {
    "electricity_single_rate": {
      "electricity_standard": 2700
    },
    "electricity_dual_rate": {
      "electricity_day": 2262,
      "electricity_night": 1638
    },
    "dual_fuel_single_rate": {
      "electricity_standard": 2700,
      "gas_standard": 11500
    },
    "dual_fuel_dual_rate": {
      "electricity_day": 2262,
      "electricity_night": 1638,
      "gas_standard": 11500
    }
  },
  "links": [
    {
      "href": "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/",
      "method": "GET",
      "rel": "self"
    }
  ],
  "brand": "OCTOPUS_ENERGY"
}
    """.trimIndent()

    val dto_oe_fix_12m_24_04_11 = SingleProductApiResponse(
        code = "OE-FIX-12M-24-04-11",
        fullName = "Octopus 12M Fixed April 2024 v1",
        displayName = "Octopus 12M Fixed",
        description = "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
        isVariable = false,
        isGreen = false,
        isTracker = false,
        isPrepay = false,
        isBusiness = false,
        isRestricted = false,
        term = 12,
        availableFrom = Instant.parse("2024-04-10T23:00:00Z"),
        availableTo = Instant.parse("2024-05-03T23:00:00Z"),
        tariffsActiveAt = Instant.parse("2024-05-13T16:00:13.822567Z"),
        singleRegisterElectricityTariffs = mapOf(
            "_A" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-1R-OE-FIX-12M-24-04-11-A",
                    standingChargeExcVat = 45.57,
                    standingChargeIncVat = 47.8485,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 23.344,
                    standardUnitRateIncVat = 24.5112,
                    dayUnitRateExcVat = null,
                    dayUnitRateIncVat = null,
                    nightUnitRateExcVat = null,
                    nightUnitRateIncVat = null,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-A/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-A/standard-unit-rates/",
                            method = "GET",
                            rel = "standard_unit_rates",
                        ),
                    ),
                ),
                varying = null,
            ),
            "_B" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-1R-OE-FIX-12M-24-04-11-B",
                    standingChargeExcVat = 51.37,
                    standingChargeIncVat = 53.9385,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 21.932,
                    standardUnitRateIncVat = 23.0286,
                    dayUnitRateExcVat = null,
                    dayUnitRateIncVat = null,
                    nightUnitRateExcVat = null,
                    nightUnitRateIncVat = null,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-B/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-B/standard-unit-rates/",
                            method = "GET",
                            rel = "standard_unit_rates",
                        ),
                    ),
                ),
                varying = null,
            ),
            "_C" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-1R-OE-FIX-12M-24-04-11-C",
                    standingChargeExcVat = 36.88,
                    standingChargeIncVat = 38.724,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = 23.788,
                    standardUnitRateIncVat = 24.9774,
                    dayUnitRateExcVat = null,
                    dayUnitRateIncVat = null,
                    nightUnitRateExcVat = null,
                    nightUnitRateIncVat = null,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-C/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-1R-OE-FIX-12M-24-04-11-C/standard-unit-rates/",
                            method = "GET",
                            rel = "standard_unit_rates",
                        ),
                    ),
                ),
                varying = null,
            ),
        ),
        dualRegisterElectricityTariffs = mapOf(
            "_A" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-2R-OE-FIX-12M-24-04-11-A",
                    standingChargeExcVat = 46.09,
                    standingChargeIncVat = 48.3945,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = null,
                    standardUnitRateIncVat = null,
                    dayUnitRateExcVat = 29.1791,
                    dayUnitRateIncVat = 30.638055,
                    nightUnitRateExcVat = 12.2402,
                    nightUnitRateIncVat = 12.85221,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-A/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-A/day-unit-rates/",
                            method = "GET",
                            rel = "day_unit_rates",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-A/night-unit-rates/",
                            method = "GET",
                            rel = "night_unit_rates",
                        ),
                    ),
                ),
                varying = null,
            ),
            "_B" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-2R-OE-FIX-12M-24-04-11-B",
                    standingChargeExcVat = 51.1,
                    standingChargeIncVat = 53.655,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = null,
                    standardUnitRateIncVat = null,
                    dayUnitRateExcVat = 27.2291,
                    dayUnitRateIncVat = 28.590555,
                    nightUnitRateExcVat = 12.1388,
                    nightUnitRateIncVat = 12.74574,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-B/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-B/day-unit-rates/",
                            method = "GET",
                            rel = "day_unit_rates",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-B/night-unit-rates/",
                            method = "GET",
                            rel = "night_unit_rates",
                        ),
                    ),
                ),
                varying = null,
            ),
            "_C" to TariffDetailsDto(
                directDebitMonthly = ElectricityTariffDto(
                    code = "E-2R-OE-FIX-12M-24-04-11-C",
                    standingChargeExcVat = 36.84,
                    standingChargeIncVat = 38.682,
                    onlineDiscountExcVat = 0.0,
                    onlineDiscountIncVat = 0.0,
                    dualFuelDiscountExcVat = 0.0,
                    dualFuelDiscountIncVat = 0.0,
                    exitFeesExcVat = 0.0,
                    exitFeesIncVat = 0.0,
                    exitFeesType = "NONE",
                    standardUnitRateExcVat = null,
                    standardUnitRateIncVat = null,
                    dayUnitRateExcVat = 29.6235,
                    dayUnitRateIncVat = 31.104675,
                    nightUnitRateExcVat = 12.5164,
                    nightUnitRateIncVat = 13.14222,
                    links = listOf(
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-C/standing-charges/",
                            method = "GET",
                            rel = "standing_charges",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-C/day-unit-rates/",
                            method = "GET",
                            rel = "day_unit_rates",
                        ),
                        LinkDto(
                            href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/electricity-tariffs/E-2R-OE-FIX-12M-24-04-11-C/night-unit-rates/",
                            method = "GET",
                            rel = "night_unit_rates",
                        ),
                    ),
                ),
                varying = null,
            ),
        ),
        brand = "OCTOPUS_ENERGY",
        links = listOf(
            LinkDto(href = "https://api.octopus.energy/v1/products/OE-FIX-12M-24-04-11/", method = "GET", rel = "self"),
        ),
        sampleQuotes = mapOf(
            "_A" to SampleQuotesDto(
                directDebitMonthly = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 83645,
                        annualCostExcVat = 79662,
                    ),
                ),
                varying = null,
            ),
            "_B" to SampleQuotesDto(
                directDebitMonthly = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 81865,
                        annualCostExcVat = 77966,
                    ),
                ),
                varying = null,
            ),
            "_C" to SampleQuotesDto(
                directDebitMonthly = QuotesDto(
                    electricitySingleRate = ElectricityRateQuotesDto(
                        annualCostIncVat = 81573,
                        annualCostExcVat = 77689,
                    ),
                ),
                varying = null,
            ),
        ),
        sampleConsumption = SampleConsumptionDto(
            electricitySingleRate = ConsumptionDetailDto(
                electricityStandard = 2700,
            ),
            electricityDualRate = DualRateConsumptionDetailDto(
                electricityDay = 2262,
                electricityNight = 1638,
                gasStandard = null,
            ),
            dualFuelSingleRate = DualFuelConsumptionDetailDto(
                electricityStandard = 2700,
                gasStandard = 11500,
            ),
            dualFuelDualRate = DualRateConsumptionDetailDto(
                electricityDay = 2262,
                electricityNight = 1638,
                gasStandard = 11500,
            ),
        ),
    )
}
