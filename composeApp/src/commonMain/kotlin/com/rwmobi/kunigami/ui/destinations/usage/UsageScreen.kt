/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.toLocalDateTimeString
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.model.RequestedChartLayout
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.utils.generateGYRHueColorPalette
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.TickPosition
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun UsageScreen(
    modifier: Modifier = Modifier,
    uiState: UsageUIState,
    uiEvent: UsageUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val dimension = LocalDensity.current.getDimension()
    val lazyListState = rememberLazyListState()
    val colorPalette = remember {
        generateGYRHueColorPalette(
            saturation = 0.6f,
            lightness = 0.6f,
        )
    }

    Box(modifier = modifier) {
        if (uiState.consumptions.isNotEmpty()) {
            val entries: List<VerticalBarPlotEntry<Int, Double>> = remember(uiState.consumptions) {
                buildList {
                    uiState.consumptions.forEachIndexed { index, consumption ->
                        add(DefaultVerticalBarPlotEntry((index + 1), DefaultVerticalBarPosition(0.0, consumption.consumption)))
                    }
                }
            }

            val labelIndex: Map<Int, Int> = remember(uiState.consumptions, uiState.requestedChartLayout) {
                buildMap {
                    // Generate all possible labels
                    var lastRateValue: Int? = null
                    uiState.consumptions.forEachIndexed { index, consumption ->
                        val currentTime = consumption.intervalStart.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                        if (currentTime != lastRateValue && currentTime % 2 == 0) {
                            put(index + 1, currentTime)
                        }
                        lastRateValue = currentTime
                    }
                }
            }

            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxSize(),
                enabled = uiState.consumptions.isNotEmpty(),
                lazyListState = lazyListState,
            ) { contentModifier ->
                LazyColumn(
                    modifier = contentModifier.fillMaxSize(),
                    state = lazyListState,
                ) {
                    item {
                        BoxWithConstraints {
                            val constraintModifier = when (uiState.requestedChartLayout) {
                                is RequestedChartLayout.Portrait -> {
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4 / 3f)
                                }

                                is RequestedChartLayout.LandScape -> {
                                    Modifier
                                        .fillMaxWidth()
                                        .height(uiState.requestedChartLayout.requestedMaxHeight)
                                }
                            }

                            VerticalBarChart(
                                modifier = constraintModifier.padding(all = dimension.grid_2),
                                entries = entries,
                                yAxisRange = uiState.consumptionRange,
                                yAxisTickPosition = TickPosition.Outside,
                                xAxisTickPosition = TickPosition.Outside,
                                yAxisTitle = "kWh",
                                barWidth = 0.8f,
                                colorPalette = colorPalette,
                                labelGenerator = { index ->
                                    labelIndex[index]?.toString()?.padStart(2, '0')
                                },
                                tooltipGenerator = { index ->
                                    with(uiState.consumptions[index]) {
                                        "${intervalStart.toLocalHourMinuteString()} - ${intervalEnd.toLocalHourMinuteString()}\n$consumption kWh"
                                    }
                                },
                            )
                        }
                    }

                    // Partition the list into columns
                    val partitionedItems = partitionList(uiState.consumptions, uiState.requestedUsageColumns)
                    val maxRows = partitionedItems.maxOf { it.size }

                    items(maxRows) { rowIndex ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
                        ) {
                            for (columnIndex in 0 until uiState.requestedUsageColumns) {
                                val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
                                if (item != null) {
                                    GridItem(consumption = item, modifier = Modifier.weight(1f))
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        } else if (!uiState.isLoading) {
            // no data
            Text("Placeholder for no data")
        }

        if (uiState.isLoading) {
            LoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }
}

@Composable
fun GridItem(
    modifier: Modifier = Modifier,
    consumption: Consumption,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.grid_2),
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = consumption.intervalStart.toLocalDateTimeString(),
        )

        Text(
            modifier = Modifier.wrapContentWidth(),
            fontWeight = FontWeight.Bold,
            text = consumption.consumption.toString(precision = 2),
        )
    }
}

fun <T> partitionList(list: List<T>, parts: Int): List<List<T>> {
    val partitionSize = (list.size + parts - 1) / parts // Ceiling division
    return list.chunked(partitionSize)
}
