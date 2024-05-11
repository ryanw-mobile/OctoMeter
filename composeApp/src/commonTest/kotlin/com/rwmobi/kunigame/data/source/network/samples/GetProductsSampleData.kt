/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.samples

import com.rwmobi.kunigami.data.source.network.dto.LinkDto
import com.rwmobi.kunigami.data.source.network.dto.ProductDetailsDto
import com.rwmobi.kunigami.data.source.network.dto.ProductsApiResponse
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
}
