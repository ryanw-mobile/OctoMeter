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

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.domain.extensions.getLocalDayMonthString
import com.rwmobi.kunigami.domain.extensions.getLocalDayOfMonth
import com.rwmobi.kunigami.domain.extensions.getLocalDayOfWeekAndDayString
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.extensions.getLocalMonthString
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.composehelper.palette.RatePalette
import com.rwmobi.kunigami.ui.composehelper.shouldUseDarkTheme
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.theme.AppTheme
import io.github.koalaplot.core.util.toString

@Composable
internal fun ConsumptionGroupCells(
    modifier: Modifier = Modifier,
    partitionedItems: List<List<Consumption>>,
    rowIndex: Int,
    shouldHideLastColumn: Boolean,
    consumptionRange: ClosedFloatingPointRange<Double>,
    presentationStyle: ConsumptionPresentationStyle,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_3),
    ) {
        val columnRangeToRender = if (shouldHideLastColumn) {
            0..<partitionedItems.lastIndex
        } else {
            partitionedItems.indices
        }

        for (columnIndex in columnRangeToRender) {
            val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
            if (item != null) {
                val label = when (presentationStyle) {
                    ConsumptionPresentationStyle.DAY_HALF_HOURLY -> item.interval.start.getLocalHHMMString()
                    ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> item.interval.start.getLocalDayOfWeekAndDayString()
                    ConsumptionPresentationStyle.MONTH_WEEKS -> item.interval.start.getLocalDayMonthString()
                    ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> item.interval.start.getLocalDayOfMonth().toString()
                    ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> item.interval.start.getLocalMonthString()
                }
                IndicatorTextValueGridItem(
                    modifier = Modifier.weight(1f),
                    label = label,
                    value = item.kWhConsumed.toString(precision = 2),
                    indicatorColor = RatePalette.lookupColorFromRange(
                        value = item.kWhConsumed,
                        range = consumptionRange,
                        shouldUseDarkTheme = shouldUseDarkTheme(),
                    ),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
