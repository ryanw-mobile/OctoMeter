/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.test.samples

import com.rwmobi.kunigami.data.source.network.dto.LinkDto
import com.rwmobi.kunigami.data.source.network.dto.products.ProductDetailsDto
import com.rwmobi.kunigami.data.source.network.dto.products.ProductsApiResponse
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import kotlinx.datetime.Instant

object GetProductsSampleData {
    val json =
        """{
    "count": 5,
    "next": null,
    "previous": null,
    "results": [
        {
            "code": "AGILE-24-04-03",
            "direction": "IMPORT",
            "full_name": "Agile Octopus April 2024 v1",
            "display_name": "Agile Octopus",
            "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            "is_variable": true,
            "is_green": true,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2024-04-03T00:00:00+01:00",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/AGILE-24-04-03/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "OCTOPUS_ENERGY"
        },
        {
            "code": "AGILE-BB-24-04-03",
            "direction": "IMPORT",
            "full_name": "Agile Octopus April 2024 v1",
            "display_name": "Agile Octopus",
            "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            "is_variable": true,
            "is_green": true,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2024-04-03T00:00:00+01:00",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/AGILE-BB-24-04-03/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "BULB"
        },
        {
            "code": "AGILE-OUTGOING-19-05-13",
            "direction": "EXPORT",
            "full_name": "Agile Outgoing Octopus May 2019",
            "display_name": "Agile Outgoing Octopus",
            "description": "Outgoing Octopus Agile rate pays you for all your exported energy based on the day-ahead wholesale rate.",
            "is_variable": true,
            "is_green": true,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2018-01-01T00:00:00Z",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/AGILE-OUTGOING-19-05-13/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "OCTOPUS_ENERGY"
        },
        {
            "code": "AGILE-OUTGOING-BB-23-02-28",
            "direction": "EXPORT",
            "full_name": "Agile Outgoing Octopus February 2023 v1",
            "display_name": "Agile Outgoing Octopus",
            "description": "Outgoing Octopus Agile rate pays you for all your exported energy based on the day-ahead wholesale rate.",
            "is_variable": true,
            "is_green": true,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2023-02-27T00:00:00Z",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/AGILE-OUTGOING-BB-23-02-28/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "BULB"
        },
        {
            "code": "COOP-FIX-12M-24-05-04",
            "direction": "IMPORT",
            "full_name": "Co-op 12M Fixed May 2024 v1",
            "display_name": "Co-op 12M Fixed",
            "description": "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
            "is_variable": false,
            "is_green": false,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2024-05-04T00:00:00+01:00",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/COOP-FIX-12M-24-05-04/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "COOP_ENERGY"
        }
    ]
}
        """.trimIndent()

    val jsonPage1 =
        """{
    "count": 2,
    "next": "https://some.endpoint.test/v1/products/AGILE-24-04-03/?page=2",
    "previous": null,
    "results": [
        {
            "code": "AGILE-24-04-03",
            "direction": "IMPORT",
            "full_name": "Agile Octopus April 2024 v1",
            "display_name": "Agile Octopus",
            "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            "is_variable": true,
            "is_green": true,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2024-04-03T00:00:00+01:00",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/AGILE-24-04-03/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "OCTOPUS_ENERGY"
        }
    ]
}
        """.trimIndent()

    val jsonPage2 =
        """{
    "count": 2,
    "next": null,
    "previous": null,
    "results": [
        {
            "code": "AGILE-BB-24-04-03",
            "direction": "IMPORT",
            "full_name": "Agile Octopus April 2024 v1",
            "display_name": "Agile Octopus",
            "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            "is_variable": true,
            "is_green": true,
            "is_tracker": false,
            "is_prepay": false,
            "is_business": false,
            "is_restricted": false,
            "term": 12,
            "available_from": "2024-04-03T00:00:00+01:00",
            "available_to": null,
            "links": [
                {
                    "href": "https://api.octopus.energy/v1/products/AGILE-BB-24-04-03/",
                    "method": "GET",
                    "rel": "self"
                }
            ],
            "brand": "BULB"
        }
    ]
}
        """.trimIndent()

    val dto = ProductsApiResponse(
        count = 5,
        next = null,
        previous = null,
        results = listOf(
            ProductDetailsDto(
                code = "AGILE-24-04-03",
                direction = "IMPORT",
                fullName = "Agile Octopus April 2024 v1",
                displayName = "Agile Octopus",
                description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                isVariable = true,
                isGreen = true,
                isTracker = false,
                isPrepay = false,
                isBusiness = false,
                isRestricted = false,
                term = 12,
                availableFrom = Instant.parse("2024-04-02T23:00:00Z"),
                availableTo = null,
                linkDtos = listOf(
                    LinkDto(
                        href = "https://api.octopus.energy/v1/products/AGILE-24-04-03/",
                        method = "GET",
                        rel = "self",
                    ),
                ),
                brand = "OCTOPUS_ENERGY",
            ),
            ProductDetailsDto(
                code = "AGILE-BB-24-04-03",
                direction = "IMPORT",
                fullName = "Agile Octopus April 2024 v1",
                displayName = "Agile Octopus",
                description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                isVariable = true,
                isGreen = true,
                isTracker = false,
                isPrepay = false,
                isBusiness = false,
                isRestricted = false,
                term = 12,
                availableFrom = Instant.parse("2024-04-02T23:00:00Z"),
                availableTo = null,
                linkDtos = listOf(
                    LinkDto(
                        href = "https://api.octopus.energy/v1/products/AGILE-BB-24-04-03/",
                        method = "GET",
                        rel = "self",
                    ),
                ),
                brand = "BULB",
            ),
            ProductDetailsDto(
                code = "AGILE-OUTGOING-19-05-13",
                direction = "EXPORT",
                fullName = "Agile Outgoing Octopus May 2019",
                displayName = "Agile Outgoing Octopus",
                description = "Outgoing Octopus Agile rate pays you for all your exported energy based on the day-ahead wholesale rate.",
                isVariable = true,
                isGreen = true,
                isTracker = false,
                isPrepay = false,
                isBusiness = false,
                isRestricted = false,
                term = 12,
                availableFrom = Instant.parse("2018-01-01T00:00:00Z"),
                availableTo = null,
                linkDtos = listOf(
                    LinkDto(
                        href = "https://api.octopus.energy/v1/products/AGILE-OUTGOING-19-05-13/",
                        method = "GET",
                        rel = "self",
                    ),
                ),
                brand = "OCTOPUS_ENERGY",
            ),
            ProductDetailsDto(
                code = "AGILE-OUTGOING-BB-23-02-28",
                direction = "EXPORT",
                fullName = "Agile Outgoing Octopus February 2023 v1",
                displayName = "Agile Outgoing Octopus",
                description = "Outgoing Octopus Agile rate pays you for all your exported energy based on the day-ahead wholesale rate.",
                isVariable = true,
                isGreen = true,
                isTracker = false,
                isPrepay = false,
                isBusiness = false,
                isRestricted = false,
                term = 12,
                availableFrom = Instant.parse("2023-02-27T00:00:00Z"),
                availableTo = null,
                linkDtos = listOf(
                    LinkDto(
                        href = "https://api.octopus.energy/v1/products/AGILE-OUTGOING-BB-23-02-28/",
                        method = "GET",
                        rel = "self",
                    ),
                ),
                brand = "BULB",
            ),
            ProductDetailsDto(
                code = "COOP-FIX-12M-24-05-04",
                direction = "IMPORT",
                fullName = "Co-op 12M Fixed May 2024 v1",
                displayName = "Co-op 12M Fixed",
                description = "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
                isVariable = false,
                isGreen = false,
                isTracker = false,
                isPrepay = false,
                isBusiness = false,
                isRestricted = false,
                term = 12,
                availableFrom = Instant.parse("2024-05-03T23:00:00Z"),
                availableTo = null,
                linkDtos = listOf(
                    LinkDto(
                        href = "https://api.octopus.energy/v1/products/COOP-FIX-12M-24-05-04/",
                        method = "GET",
                        rel = "self",
                    ),
                ),
                brand = "COOP_ENERGY",
            ),
        ),
    )

    val energyProductsQueryResponse = """{
  "data": {
    "energyProducts": {
      "edges": [
        {
          "node": {
            "code": "OE-FIX-12M-24-07-25",
            "direction": "IMPORT",
            "fullName": "Octopus 12M Fixed July 2024 v1",
            "displayName": "Octopus 12M Fixed",
            "description": "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
            "term": 12,
            "availableTo": null,
            "availableFrom": "2024-07-25T00:00:00+01:00",
            "isVariable": false,
            "isChargedHalfHourly": false,
            "isGreen": false,
            "isPrepay": false,
            "isBusiness": false
          }
        },
        {
          "node": {
            "code": "VAR-22-11-01",
            "direction": "IMPORT",
            "fullName": "Flexible Octopus",
            "displayName": "Flexible Octopus",
            "description": "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
            "term": null,
            "availableTo": null,
            "availableFrom": "2023-03-28T10:35:00+01:00",
            "isVariable": true,
            "isChargedHalfHourly": false,
            "isGreen": false,
            "isPrepay": false,
            "isBusiness": false
          }
        },
        {
          "node": {
            "code": "AGILE-24-04-03",
            "direction": "IMPORT",
            "fullName": "Agile Octopus April 2024 v1",
            "displayName": "Agile Octopus",
            "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            "term": 12,
            "availableTo": null,
            "availableFrom": "2024-04-03T00:00:00+01:00",
            "isVariable": true,
            "isChargedHalfHourly": true,
            "isGreen": true,
            "isPrepay": false,
            "isBusiness": false
          }
        },
        {
          "node": {
            "code": "INTELLI-FLUX-IMPORT-23-07-14",
            "direction": "IMPORT",
            "fullName": "Intelligent Octopus Flux Import",
            "displayName": "Intelligent Octopus Flux Import",
            "description": "Power your home with 100% renewable energy on this Octopus Energy electricity tariff designed exclusively for solar and battery owners.",
            "term": null,
            "availableTo": null,
            "availableFrom": "2023-07-13T00:00:00+01:00",
            "isVariable": true,
            "isChargedHalfHourly": true,
            "isGreen": false,
            "isPrepay": false,
            "isBusiness": false
          }
        },
        {
          "node": {
            "code": "FLUX-IMPORT-23-02-14",
            "direction": "IMPORT",
            "fullName": "Octopus Flux Import",
            "displayName": "Octopus Flux Import",
            "description": "Power your home with 100% renewable energy on this Octopus Energy electricity tariff designed exclusively for solar and battery owners.",
            "term": null,
            "availableTo": null,
            "availableFrom": "2023-02-14T00:00:00+00:00",
            "isVariable": true,
            "isChargedHalfHourly": true,
            "isGreen": false,
            "isPrepay": false,
            "isBusiness": false
          }
        },
        {
          "node": {
            "code": "COSY-22-12-08",
            "direction": "IMPORT",
            "fullName": "Cosy Octopus",
            "displayName": "Cosy Octopus",
            "description": "Cosy Octopus is a heat pump tariff with six hours of super cheap electricity every day to warm your home.",
            "term": null,
            "availableTo": null,
            "availableFrom": "2022-12-13T00:00:00+00:00",
            "isVariable": true,
            "isChargedHalfHourly": true,
            "isGreen": false,
            "isPrepay": false,
            "isBusiness": false
          }
        }
      ],
      "totalCount": 13,
      "pageInfo": {
        "hasNextPage": false,
        "endCursor": "YXJyYXljb25uZWN0aW9uOjEy"
      }
    }
  }
}
    """.trimIndent()

    val productSummary = listOf(
        ProductSummary(
            code = "OE-FIX-12M-24-07-25",
            direction = ProductDirection.IMPORT,
            fullName = "Octopus 12M Fixed July 2024 v1",
            displayName = "Octopus 12M Fixed",
            description = "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
            features = emptyList(),
            term = 12,
            availability = Instant.parse("2024-07-24T23:00:00Z")..Instant.DISTANT_FUTURE,
            brand = "OCTOPUS_ENERGY",
        ),
        ProductSummary(
            code = "VAR-22-11-01",
            direction = ProductDirection.IMPORT,
            fullName = "Flexible Octopus",
            displayName = "Flexible Octopus",
            description = "Flexible Octopus offers great value and 100% renewable electricity. As a variable tariff, your prices can rise and fall with wholesale prices - but we'll always give you notice of a change.",
            features = listOf(ProductFeature.VARIABLE),
            term = null,
            availability = Instant.parse("2023-03-28T09:35:00Z")..Instant.DISTANT_FUTURE,
            brand = "OCTOPUS_ENERGY",
        ),
        ProductSummary(
            code = "AGILE-24-04-03",
            direction = ProductDirection.IMPORT,
            fullName = "Agile Octopus April 2024 v1",
            displayName = "Agile Octopus",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            features = listOf(ProductFeature.VARIABLE, ProductFeature.GREEN, ProductFeature.CHARGEDHALFHOURLY),
            term = 12,
            availability = Instant.parse("2024-04-02T23:00:00Z")..Instant.DISTANT_FUTURE,
            brand = "OCTOPUS_ENERGY",
        ),
        ProductSummary(
            code = "INTELLI-FLUX-IMPORT-23-07-14",
            direction = ProductDirection.IMPORT,
            fullName = "Intelligent Octopus Flux Import",
            displayName = "Intelligent Octopus Flux Import",
            description = "Power your home with 100% renewable energy on this Octopus Energy electricity tariff designed exclusively for solar and battery owners.",
            features = listOf(ProductFeature.VARIABLE, ProductFeature.CHARGEDHALFHOURLY),
            term = null,
            availability = Instant.parse("2023-07-12T23:00:00Z")..Instant.DISTANT_FUTURE,
            brand = "OCTOPUS_ENERGY",
        ),
        ProductSummary(
            code = "FLUX-IMPORT-23-02-14",
            direction = ProductDirection.IMPORT,
            fullName = "Octopus Flux Import",
            displayName = "Octopus Flux Import",
            description = "Power your home with 100% renewable energy on this Octopus Energy electricity tariff designed exclusively for solar and battery owners.",
            features = listOf(ProductFeature.VARIABLE, ProductFeature.CHARGEDHALFHOURLY),
            term = null,
            availability = Instant.parse("2023-02-14T00:00:00Z")..Instant.DISTANT_FUTURE,
            brand = "OCTOPUS_ENERGY",
        ),
        ProductSummary(
            code = "COSY-22-12-08",
            direction = ProductDirection.IMPORT,
            fullName = "Cosy Octopus",
            displayName = "Cosy Octopus",
            description = "Cosy Octopus is a heat pump tariff with six hours of super cheap electricity every day to warm your home.",
            features = listOf(ProductFeature.VARIABLE, ProductFeature.CHARGEDHALFHOURLY),
            term = null,
            availability = Instant.parse("2022-12-13T00:00:00Z")..Instant.DISTANT_FUTURE,
            brand = "OCTOPUS_ENERGY",
        ),
    )

    val energyProductsQueryResponsePage1 = """{
  "data": {
    "energyProducts": {
      "edges": [
        {
          "node": {
            "code": "OE-FIX-12M-24-07-25",
            "direction": "IMPORT",
            "fullName": "Octopus 12M Fixed July 2024 v1",
            "displayName": "Octopus 12M Fixed",
            "description": "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
            "term": 12,
            "availableTo": null,
            "availableFrom": "2024-07-25T00:00:00+01:00",
            "isVariable": false,
            "isChargedHalfHourly": false,
            "isGreen": false,
            "isPrepay": false,
            "isBusiness": false
          }
        }
      ],
      "totalCount": 2,
      "pageInfo": {
        "hasNextPage": true,
        "endCursor": "YXJyYXljb25uZWN0aW9uOjA="
      }
    }
  }
}
    """.trimIndent()

    val energyProductsQueryResponsePage2 = """{
  "data": {
    "energyProducts": {
      "edges": [
        {
          "node": {
            "code": "AGILE-24-04-03",
            "direction": "IMPORT",
            "fullName": "Agile Octopus April 2024 v1",
            "displayName": "Agile Octopus",
            "description": "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            "term": 12,
            "availableTo": null,
            "availableFrom": "2024-04-03T00:00:00+01:00",
            "isVariable": true,
            "isChargedHalfHourly": true,
            "isGreen": true,
            "isPrepay": false,
            "isBusiness": false
          }        
        }
      ],
      "totalCount": 2,
      "pageInfo": {
        "hasNextPage": false,
        "endCursor": "YXJyYXljb25uZWN0aW9uOjE="
      }
    }
  }
}
    """.trimIndent()

    val productSummaryPage1 = ProductSummary(
        code = "OE-FIX-12M-24-07-25",
        direction = ProductDirection.IMPORT,
        fullName = "Octopus 12M Fixed July 2024 v1",
        displayName = "Octopus 12M Fixed",
        description = "This tariff features 100% renewable electricity and fixes your unit rates and standing charge for 12 months.",
        features = emptyList(),
        term = 12,
        availability = Instant.parse("2024-07-24T23:00:00Z")..Instant.DISTANT_FUTURE,
        brand = "OCTOPUS_ENERGY",
    )

    val productSummaryPage2 = ProductSummary(
        code = "AGILE-24-04-03",
        direction = ProductDirection.IMPORT,
        fullName = "Agile Octopus April 2024 v1",
        displayName = "Agile Octopus",
        description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
        features = listOf(ProductFeature.VARIABLE, ProductFeature.GREEN, ProductFeature.CHARGEDHALFHOURLY),
        term = 12,
        availability = Instant.parse("2024-04-02T23:00:00Z")..Instant.DISTANT_FUTURE,
        brand = "OCTOPUS_ENERGY",
    )

    val productSummaryTwoPages = listOf(
        productSummaryPage1,
        productSummaryPage2,
    )
}
