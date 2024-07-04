/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.chart

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.rate.Rate
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_chart_tooltip_range_p
import org.jetbrains.compose.resources.getString

@Immutable
data class BarChartData(
    val verticalBarPlotEntries: List<VerticalBarPlotEntry<Int, Double>>,
    val labels: Map<Int, String>,
    val tooltips: List<String>,
) {
    companion object {
        suspend fun fromRates(rates: List<Rate>) = BarChartData(
            verticalBarPlotEntries = getVerticalBarPlotEntries(rates = rates),
            labels = generateChartLabels(rates = rates),
            tooltips = generateChartToolTips(rates = rates),
        )

        private fun getVerticalBarPlotEntries(rates: List<Rate>): List<VerticalBarPlotEntry<Int, Double>> = buildList {
            rates.forEachIndexed { index, rate ->
                add(
                    element = DefaultVerticalBarPlotEntry(
                        x = index,
                        y = if (rate.vatInclusivePrice >= 0) {
                            DefaultVerticalBarPosition(yMin = 0.0, yMax = rate.vatInclusivePrice)
                        } else {
                            DefaultVerticalBarPosition(yMin = rate.vatInclusivePrice, yMax = 0.0)
                        },
                    ),
                )
            }
        }

        private fun generateChartLabels(rates: List<Rate>): Map<Int, String> {
            return buildMap {
                var lastRateValue: Int? = null
                rates.forEachIndexed { index, rate ->
                    val currentTime = rate.validity.start.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                    if (currentTime != lastRateValue) {
                        put(key = index, value = currentTime.toString().padStart(length = 2, padChar = '0'))
                        lastRateValue = currentTime
                    }
                }
            }
        }

        private suspend fun generateChartToolTips(rates: List<Rate>): List<String> {
            return rates.map { rate ->
                val validToString = if (rate.validity.endInclusive != Instant.DISTANT_FUTURE) {
                    " - ${rate.validity.endInclusive.getLocalHHMMString()}"
                } else {
                    ""
                }
                val timeRange = rate.validity.start.getLocalHHMMString() + validToString

                getString(
                    resource = Res.string.agile_chart_tooltip_range_p,
                    timeRange,
                    rate.vatInclusivePrice.roundToTwoDecimalPlaces(),
                )
            }
        }
    }
}
