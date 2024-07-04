/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.chart

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.getLocalDayMonthString
import com.rwmobi.kunigami.domain.extensions.getLocalDayOfMonth
import com.rwmobi.kunigami.domain.extensions.getLocalEnglishAbbreviatedDayOfWeekName
import com.rwmobi.kunigami.domain.extensions.getLocalHHMMString
import com.rwmobi.kunigami.domain.extensions.getLocalMonthString
import com.rwmobi.kunigami.domain.extensions.getLocalMonthYearString
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.util.toString
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_chart_tooltip_range_p
import kunigami.composeapp.generated.resources.usage_chart_tooltip_range_kwh
import kunigami.composeapp.generated.resources.usage_chart_tooltip_spot_kwh
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration.Companion.nanoseconds

@Immutable
data class BarChartData(
    val verticalBarPlotEntries: List<VerticalBarPlotEntry<Int, Double>>,
    val labels: Map<Int, String>,
    val tooltips: List<String>,
) {
    companion object {
        suspend fun fromRates(rates: List<Rate>) = BarChartData(
            verticalBarPlotEntries = generateRateVerticalBarPlotEntries(rates = rates),
            labels = generateRateLabels(rates = rates),
            tooltips = generateRateToolTips(rates = rates),
        )

        suspend fun fromConsumptions(presentationStyle: ConsumptionPresentationStyle, consumptions: List<Consumption>) = BarChartData(
            verticalBarPlotEntries = generateConsumptionVerticalBarPlotEntries(consumptions = consumptions),
            labels = generateConsumptionLabels(presentationStyle = presentationStyle, consumptions = consumptions),
            tooltips = generateConsumptionToolTips(presentationStyle = presentationStyle, consumptions = consumptions),
        )

        // Rate - Agile
        private fun generateRateVerticalBarPlotEntries(rates: List<Rate>): List<VerticalBarPlotEntry<Int, Double>> = buildList {
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

        private fun generateRateLabels(rates: List<Rate>): Map<Int, String> {
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

        private suspend fun generateRateToolTips(rates: List<Rate>): List<String> {
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

        // Consumption - Usage
        private fun generateConsumptionVerticalBarPlotEntries(consumptions: List<Consumption>): List<VerticalBarPlotEntry<Int, Double>> = buildList {
            consumptions.forEachIndexed { index, consumption ->
                add(
                    element = DefaultVerticalBarPlotEntry(
                        x = index,
                        y = DefaultVerticalBarPosition(yMin = 0.0, yMax = consumption.kWhConsumed),
                    ),
                )
            }
        }

        private fun generateConsumptionLabels(
            presentationStyle: ConsumptionPresentationStyle,
            consumptions: List<Consumption>,
        ): Map<Int, String> {
            return buildMap {
                when (presentationStyle) {
                    ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                        var lastRateValue: Int? = null
                        consumptions.forEachIndexed { index, consumption ->
                            val currentTime = consumption.interval.start.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                            if (currentTime != lastRateValue && currentTime % 2 == 0) {
                                put(
                                    key = index,
                                    value = currentTime.toString().padStart(length = 2, padChar = '0'),
                                )
                            }
                            lastRateValue = currentTime
                        }
                    }

                    ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                        consumptions.forEachIndexed { index, consumption ->
                            put(
                                key = index,
                                value = consumption.interval.start.getLocalEnglishAbbreviatedDayOfWeekName(),
                            )
                        }
                    }

                    ConsumptionPresentationStyle.MONTH_WEEKS -> {
                        consumptions.forEachIndexed { index, consumption ->
                            put(
                                key = index,
                                value = consumption.interval.start.getLocalDayMonthString(),
                            )
                        }
                    }

                    ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                        consumptions.forEachIndexed { index, consumption ->
                            put(
                                key = index,
                                value = consumption.interval.start.getLocalDayOfMonth().toString(),
                            )
                        }
                    }

                    ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                        consumptions.forEachIndexed { index, consumption ->
                            put(
                                key = index,
                                value = consumption.interval.start.getLocalMonthString(),
                            )
                        }
                    }
                }
            }
        }

        private suspend fun generateConsumptionToolTips(
            presentationStyle: ConsumptionPresentationStyle,
            consumptions: List<Consumption>,
        ): List<String> {
            return when (presentationStyle) {
                ConsumptionPresentationStyle.DAY_HALF_HOURLY -> {
                    consumptions.map { consumption ->
                        getString(
                            resource = Res.string.usage_chart_tooltip_range_kwh,
                            consumption.interval.start.getLocalHHMMString(),
                            consumption.interval.endInclusive.getLocalHHMMString(),
                            consumption.kWhConsumed.toString(precision = 2),
                        )
                    }
                }

                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> {
                    consumptions.map { consumption ->
                        getString(
                            resource = Res.string.usage_chart_tooltip_spot_kwh,
                            consumption.interval.start.getLocalDateString(),
                            consumption.kWhConsumed.toString(precision = 2),
                        )
                    }
                }

                ConsumptionPresentationStyle.MONTH_WEEKS -> {
                    consumptions.map { consumption ->
                        getString(
                            resource = Res.string.usage_chart_tooltip_range_kwh,
                            consumption.interval.start.getLocalDayMonthString(),
                            (consumption.interval.endInclusive - 1.nanoseconds).getLocalDayMonthString(),
                            consumption.kWhConsumed.toString(precision = 2),
                        )
                    }
                }

                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> {
                    consumptions.map { consumption ->
                        getString(
                            resource = Res.string.usage_chart_tooltip_spot_kwh,
                            consumption.interval.start.getLocalDayMonthString(),
                            consumption.kWhConsumed.toString(precision = 2),
                        )
                    }
                }

                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> {
                    consumptions.map { consumption ->
                        getString(
                            resource = Res.string.usage_chart_tooltip_spot_kwh,
                            consumption.interval.start.getLocalMonthYearString(),
                            consumption.kWhConsumed.toString(precision = 2),
                        )
                    }
                }
            }
        }
    }
}
