/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.product

import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.retail_region_east_midlands
import kunigami.composeapp.generated.resources.retail_region_eastern_england
import kunigami.composeapp.generated.resources.retail_region_london
import kunigami.composeapp.generated.resources.retail_region_merseyside_northern_wales
import kunigami.composeapp.generated.resources.retail_region_north_eastern_england
import kunigami.composeapp.generated.resources.retail_region_north_western_england
import kunigami.composeapp.generated.resources.retail_region_northern_scotland
import kunigami.composeapp.generated.resources.retail_region_south_eastern_england
import kunigami.composeapp.generated.resources.retail_region_south_western_england
import kunigami.composeapp.generated.resources.retail_region_southern_england
import kunigami.composeapp.generated.resources.retail_region_southern_scotland
import kunigami.composeapp.generated.resources.retail_region_southern_wales
import kunigami.composeapp.generated.resources.retail_region_west_midlands
import kunigami.composeapp.generated.resources.retail_region_yorkshire
import org.jetbrains.compose.resources.StringResource

/***
 * Retail Region with sampled postcode mapped to satisfy GraphQL queries
 */
enum class RetailRegion(val code: String, val postcode: String, val stringResource: StringResource) {
    EASTERN_ENGLAND(
        code = "A",
        postcode = "NR1",
        stringResource = Res.string.retail_region_eastern_england,
    ),
    EAST_MIDLANDS(
        code = "B",
        postcode = "NG1",
        stringResource = Res.string.retail_region_east_midlands,
    ),
    LONDON(
        code = "C",
        postcode = "E5",
        stringResource = Res.string.retail_region_london,
    ),
    MERSEYSIDE_NORTHERN_WALES(
        code = "D",
        postcode = "L1",
        stringResource = Res.string.retail_region_merseyside_northern_wales,
    ),
    WEST_MIDLANDS(
        code = "E",
        postcode = "B5",
        stringResource = Res.string.retail_region_west_midlands,
    ),
    NORTH_EASTERN_ENGLAND(
        code = "F",
        postcode = "NE1",
        stringResource = Res.string.retail_region_north_eastern_england,
    ),
    NORTH_WESTERN_ENGLAND(
        code = "G",
        postcode = "M1",
        stringResource = Res.string.retail_region_north_western_england,
    ),
    SOUTHERN_ENGLAND(
        code = "H",
        postcode = "SO14",
        stringResource = Res.string.retail_region_southern_england,
    ),
    SOUTH_EASTERN_ENGLAND(
        code = "J",
        postcode = "BN1",
        stringResource = Res.string.retail_region_south_eastern_england,
    ),
    SOUTHERN_WALES(
        code = "K",
        postcode = "CF10",
        stringResource = Res.string.retail_region_southern_wales,
    ),
    SOUTH_WESTERN_ENGLAND(
        code = "L",
        postcode = "BS5",
        stringResource = Res.string.retail_region_south_western_england,
    ),
    SOUTHERN_SCOTLAND(
        code = "N",
        postcode = "G1",
        stringResource = Res.string.retail_region_southern_scotland,
    ),
    YORKSHIRE(
        code = "M",
        postcode = "LS5",
        stringResource = Res.string.retail_region_yorkshire,
    ),
    NORTHERN_SCOTLAND(
        code = "P",
        postcode = "AB10",
        stringResource = Res.string.retail_region_northern_scotland,
    ),
    ;

    companion object {
        fun fromCode(code: String?): RetailRegion? {
            return entries.find { it.code == code }
        }
    }
}
