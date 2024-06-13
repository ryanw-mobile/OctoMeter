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

enum class RetailRegion(val code: String, val stringResource: StringResource) {
    EASTERN_ENGLAND("A", Res.string.retail_region_eastern_england),
    EAST_MIDLANDS("B", Res.string.retail_region_east_midlands),
    LONDON("C", Res.string.retail_region_london),
    MERSEYSIDE_NORTHERN_WALES("D", Res.string.retail_region_merseyside_northern_wales),
    WEST_MIDLANDS("E", Res.string.retail_region_west_midlands),
    NORTH_EASTERN_ENGLAND("F", Res.string.retail_region_north_eastern_england),
    NORTH_WESTERN_ENGLAND("G", Res.string.retail_region_north_western_england),
    SOUTHERN_ENGLAND("H", Res.string.retail_region_southern_england),
    SOUTH_EASTERN_ENGLAND("J", Res.string.retail_region_south_eastern_england),
    SOUTHERN_WALES("K", Res.string.retail_region_southern_wales),
    SOUTH_WESTERN_ENGLAND("L", Res.string.retail_region_south_western_england),
    SOUTHERN_SCOTLAND("N", Res.string.retail_region_southern_scotland),
    YORKSHIRE("M", Res.string.retail_region_yorkshire),
    NORTHERN_SCOTLAND("P", Res.string.retail_region_northern_scotland),
    ;

    companion object {
        fun fromCode(code: String?): RetailRegion? {
            return entries.find { it.code == code }
        }
    }
}
