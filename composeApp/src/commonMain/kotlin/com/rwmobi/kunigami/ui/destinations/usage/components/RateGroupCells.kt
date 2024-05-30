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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.extensions.toLocalDay
import com.rwmobi.kunigami.domain.extensions.toLocalDayMonth
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.extensions.toLocalMonth
import com.rwmobi.kunigami.domain.extensions.toLocalWeekdayDay
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.extensions.getPercentageColorIndex
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString

@Composable
internal fun RateGroupCells(
    modifier: Modifier = Modifier,
    partitionedItems: List<List<Consumption>>,
    rowIndex: Int,
    maxInRange: Double,
    presentationStyle: ConsumptionPresentationStyle,
    colorPalette: List<Color>,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
    ) {
        for (columnIndex in partitionedItems.indices) {
            val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
            if (item != null) {
                val label = when (presentationStyle) {
                    ConsumptionPresentationStyle.DAY_HALF_HOURLY -> item.intervalStart.toLocalHourMinuteString()
                    ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> item.intervalStart.toLocalWeekdayDay()
                    ConsumptionPresentationStyle.MONTH_WEEKS -> item.intervalStart.toLocalDayMonth()
                    ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> item.intervalStart.toLocalDay()
                    ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> item.intervalStart.toLocalMonth()
                }
                IndicatorTextValueGridItem(
                    modifier = Modifier.weight(1f),
                    label = label,
                    value = item.consumption.toString(precision = 2),
                    indicatorColor = colorPalette[
                        item.consumption.getPercentageColorIndex(maxValue = maxInRange),
                    ],
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
