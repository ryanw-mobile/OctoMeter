/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
import com.rwmobi.kunigami.ui.theme.getDimension
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
                val label = when (presentationStyle) {
                    ConsumptionPresentationStyle.DAY_HALF_HOURLY -> item.intervalStart.getLocalHHMMString()
                    ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> item.intervalStart.getLocalDayOfWeekAndDayString()
                    ConsumptionPresentationStyle.MONTH_WEEKS -> item.intervalStart.getLocalDayMonthString()
                    ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> item.intervalStart.getLocalDayOfMonth().toString()
                    ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> item.intervalStart.getLocalMonthString()
                }
                IndicatorTextValueGridItem(
                    modifier = Modifier.weight(1f),
                    label = label,
                    value = item.consumption.toString(precision = 2),
                    indicatorColor = RatePalette.lookupColorFromRange(
                        value = item.consumption,
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
