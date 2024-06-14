/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.composehelper.palette.RatePalette
import com.rwmobi.kunigami.ui.composehelper.shouldUseDarkTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString

@Composable
internal fun RateGroupCells(
    modifier: Modifier = Modifier,
    partitionedItems: List<List<Rate>>,
    rowIndex: Int,
    shouldHideLastColumn: Boolean,
    rateRange: ClosedFloatingPointRange<Double>,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
    ) {
        val columnRangeToRender = if (shouldHideLastColumn) {
            0..<partitionedItems.lastIndex
        } else {
            partitionedItems.indices
        }

        for (columnIndex in columnRangeToRender) {
            val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
            if (item != null) {
                IndicatorTextValueGridItem(
                    modifier = Modifier.weight(1f),
                    indicatorColor = RatePalette.lookupColorFromRange(
                        value = item.vatInclusivePrice,
                        range = rateRange,
                        shouldUseDarkTheme = shouldUseDarkTheme(),
                    ),
                    label = item.validFrom.getLocalHHMMString(),
                    value = item.vatInclusivePrice.roundToTwoDecimalPlaces()
                        .toString(precision = 2),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
