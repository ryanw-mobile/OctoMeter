/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import kotlinx.datetime.Instant

object GetTariffSampleData {
    val json = """{
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
        "tariffs_active_at": "2024-06-23T00:24:55.986143Z",
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
                    "standard_unit_rate_exc_vat": 15.79,
                    "standard_unit_rate_inc_vat": 16.5795
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
                    "standard_unit_rate_exc_vat": 15.04,
                    "standard_unit_rate_inc_vat": 15.792
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
                    "standard_unit_rate_exc_vat": 15.04,
                    "standard_unit_rate_inc_vat": 15.792
                }
            },
            "_D": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-D",
                    "standing_charge_exc_vat": 44.72,
                    "standing_charge_inc_vat": 46.956,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-D/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-D/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 16.54,
                    "standard_unit_rate_inc_vat": 17.367
                }
            },
            "_E": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-E",
                    "standing_charge_exc_vat": 45.24,
                    "standing_charge_inc_vat": 47.502,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-E/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-E/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 15.79,
                    "standard_unit_rate_inc_vat": 16.5795
                }
            },
            "_F": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-F",
                    "standing_charge_exc_vat": 45.99,
                    "standing_charge_inc_vat": 48.2895,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-F/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-F/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 15.79,
                    "standard_unit_rate_inc_vat": 16.5795
                }
            },
            "_G": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-G",
                    "standing_charge_exc_vat": 39.63,
                    "standing_charge_inc_vat": 41.6115,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-G/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-G/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 15.79,
                    "standard_unit_rate_inc_vat": 16.5795
                }
            },
            "_H": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-H",
                    "standing_charge_exc_vat": 40.73,
                    "standing_charge_inc_vat": 42.7665,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-H/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-H/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 15.79,
                    "standard_unit_rate_inc_vat": 16.5795
                }
            },
            "_J": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-J",
                    "standing_charge_exc_vat": 39.08,
                    "standing_charge_inc_vat": 41.034,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-J/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-J/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 16.54,
                    "standard_unit_rate_inc_vat": 17.367
                }
            },
            "_K": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-K",
                    "standing_charge_exc_vat": 45.26,
                    "standing_charge_inc_vat": 47.523,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-K/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-K/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 16.54,
                    "standard_unit_rate_inc_vat": 17.367
                }
            },
            "_L": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-L",
                    "standing_charge_exc_vat": 48.57,
                    "standing_charge_inc_vat": 50.9985,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-L/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-L/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 17.3,
                    "standard_unit_rate_inc_vat": 18.165
                }
            },
            "_M": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-M",
                    "standing_charge_exc_vat": 45.62,
                    "standing_charge_inc_vat": 47.901,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-M/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-M/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 15.04,
                    "standard_unit_rate_inc_vat": 15.792
                }
            },
            "_N": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-N",
                    "standing_charge_exc_vat": 46.69,
                    "standing_charge_inc_vat": 49.0245,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-N/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-N/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 15.79,
                    "standard_unit_rate_inc_vat": 16.5795
                }
            },
            "_P": {
                "direct_debit_monthly": {
                    "code": "E-1R-AGILE-FLEX-22-11-25-P",
                    "standing_charge_exc_vat": 47.08,
                    "standing_charge_inc_vat": 49.434,
                    "online_discount_exc_vat": 0,
                    "online_discount_inc_vat": 0,
                    "dual_fuel_discount_exc_vat": 0,
                    "dual_fuel_discount_inc_vat": 0,
                    "exit_fees_exc_vat": 0,
                    "exit_fees_inc_vat": 0,
                    "exit_fees_type": "NONE",
                    "links": [
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-P/standing-charges/",
                            "method": "GET",
                            "rel": "standing_charges"
                        },
                        {
                            "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/electricity-tariffs/E-1R-AGILE-FLEX-22-11-25-P/standard-unit-rates/",
                            "method": "GET",
                            "rel": "standard_unit_rates"
                        }
                    ],
                    "standard_unit_rate_exc_vat": 18.05,
                    "standard_unit_rate_inc_vat": 18.9525
                }
            }
        },
        "dual_register_electricity_tariffs": {},
        "single_register_gas_tariffs": {},
        "sample_quotes": {
            "_A": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 58378,
                        "annual_cost_exc_vat": 55598
                    }
                }
            },
            "_B": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 58743,
                        "annual_cost_exc_vat": 55945
                    }
                }
            },
            "_C": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 54140,
                        "annual_cost_exc_vat": 51562
                    }
                }
            },
            "_D": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 64030,
                        "annual_cost_exc_vat": 60981
                    }
                }
            },
            "_E": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 62103,
                        "annual_cost_exc_vat": 59146
                    }
                }
            },
            "_F": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 62390,
                        "annual_cost_exc_vat": 59419
                    }
                }
            },
            "_G": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 59953,
                        "annual_cost_exc_vat": 57098
                    }
                }
            },
            "_H": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 60374,
                        "annual_cost_exc_vat": 57499
                    }
                }
            },
            "_J": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 61868,
                        "annual_cost_exc_vat": 58922
                    }
                }
            },
            "_K": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 64237,
                        "annual_cost_exc_vat": 61178
                    }
                }
            },
            "_L": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 67660,
                        "annual_cost_exc_vat": 64438
                    }
                }
            },
            "_M": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 60122,
                        "annual_cost_exc_vat": 57259
                    }
                }
            },
            "_N": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 62659,
                        "annual_cost_exc_vat": 59675
                    }
                }
            },
            "_P": {
                "direct_debit_monthly": {
                    "electricity_single_rate": {
                        "annual_cost_inc_vat": 69215,
                        "annual_cost_exc_vat": 65919
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
                "href": "https://api.octopus.energy/v1/products/AGILE-FLEX-22-11-25/",
                "method": "GET",
                "rel": "self"
            }
        ],
        "brand": "OCTOPUS_ENERGY"
    }
    """.trimIndent()

    val singleEnergyProductQueryResponse = """{
  "data": {
    "energyProduct": {
      "code": "AGILE-FLEX-22-11-25",
      "fullName": "Agile Octopus November 2022 v1",
      "displayName": "Agile Octopus",
      "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
      "term": null,
      "isVariable": true,
      "isChargedHalfHourly": true,
      "isGreen": true,
      "isPrepay": false,
      "isBusiness": false,
      "availableTo": "2023-12-11T12:00:00+00:00",
      "availableFrom": "2022-11-25T00:00:00+00:00",
      "exitFeesType": "NONE",
      "exitFees": 0,
      "tariffs": {
        "edges": [
          {
            "node": {
              "__typename": "StandardTariff",
              "tariffCode": "E-1R-AGILE-FLEX-22-11-25-C",
              "standingCharge": 31.51,
              "unitRate": 30.66
            }
          }
        ],
        "totalCount": 1,
        "pageInfo": {
          "hasNextPage": false,
          "endCursor": "YXJyYXljb25uZWN0aW9uOjA="
        }
      }
    }
  }
}
    """.trimIndent()

    val tariff = Tariff(
        productCode = "AGILE-FLEX-22-11-25",
        fullName = "Agile Octopus November 2022 v1",
        displayName = "Agile Octopus",
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        isVariable = true,
        availability = Instant.parse("2022-11-25T00:00:00Z")..Instant.parse("2023-12-11T12:00:00Z"),
        tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
        tariffCode = "E-1R-AGILE-FLEX-22-11-25-C",
        vatInclusiveStandingCharge = 31.51,
        exitFeesType = ExitFeesType.NONE,
        vatInclusiveExitFees = 0.0,
        vatInclusiveStandardUnitRate = 30.66,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
    )
}
