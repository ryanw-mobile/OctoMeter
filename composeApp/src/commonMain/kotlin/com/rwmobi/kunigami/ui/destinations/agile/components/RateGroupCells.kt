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

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
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
    minimumVatInclusivePrice: Double,
    blinkingAlpha: Float = 1f,
) {
    val dimension = getScreenSizeInfo().getDimension()

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
                val roundedVatInclusivePriceString = item.vatInclusivePrice.toString(precision = 2)
                val shouldBlinkValue = item.vatInclusivePrice == minimumVatInclusivePrice

                IndicatorTextValueGridItem(
                    modifier = Modifier.weight(1f),
                    indicatorColor = RatePalette.lookupColorFromRange(
                        value = item.vatInclusivePrice,
                        range = rateRange,
                        shouldUseDarkTheme = shouldUseDarkTheme(),
                    ),
                    label = item.validity.start.getLocalHHMMString(),
                    value = roundedVatInclusivePriceString,
                    blinkingAlpha = if (shouldBlinkValue) blinkingAlpha else 1f,
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
