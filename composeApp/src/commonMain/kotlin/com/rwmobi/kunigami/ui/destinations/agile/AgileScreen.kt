/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.formatInstantWithoutSeconds
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.BarSamplePlot
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.TickPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AgileScreen(
    modifier: Modifier = Modifier,
    uiState: AgileUIState,
    uiEvent: AgileUIEvent,
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

    if (!uiState.isLoading) {
        val entries: List<VerticalBarPlotEntry<Int, Double>> = remember(uiState.rates) {
            buildList {
                uiState.rates.forEachIndexed { index, rate ->
                    add(DefaultVerticalBarPlotEntry((index + 1), DefaultVerticalBarPosition(0.0, rate.vatInclusivePrice)))
                }
            }
        }
        val labels: Map<Int, String> = remember(uiState.rates) {
            buildMap {
                var lastRateValue: String? = null

                uiState.rates.forEachIndexed { index, rate ->
                    val currentTime = rate.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour.toString()
                    if (currentTime != lastRateValue) {
                        put(index + 1, currentTime)
                        lastRateValue = currentTime
                    }
                }
            }
        }

        ScrollbarMultiplatform(
            modifier = modifier,
            enabled = uiState.rates.isNotEmpty(),
            lazyListState = lazyListState,
        ) { contentModifier ->
            LazyColumn(
                modifier = contentModifier.fillMaxSize(),
                state = lazyListState,
            ) {
                item {
                    BoxWithConstraints {
                        val constraintModifier = when (uiState.requestedLayout) {
                            is AgileScreenLayout.Portrait -> {
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(4 / 3f)
                            }

                            is AgileScreenLayout.LandScape -> {
                                Modifier.fillMaxSize()
                                    .height(uiState.requestedLayout.requestedMaxHeight)
                            }
                        }

                        BarSamplePlot(
                            modifier = constraintModifier.padding(all = dimension.grid_2),
                            entries = entries,
                            labels = labels,
                            yAxisRange = uiState.rateRange,
                            yAxisTickPosition = TickPosition.Outside,
                            xAxisTickPosition = TickPosition.Outside,
                            yAxisTitle = "VAT Unit Rate (p/kWh)",
                            xAxisTitle = "${uiState.rates.first().validFrom.formatInstantWithoutSeconds()} - ${uiState.rates.last().validTo?.formatInstantWithoutSeconds()}",
                            barWidth = 0.8f,
                            backgroundPlot = { graphScope ->
                                graphScope.LinePlot(
                                    modifier = Modifier.fillMaxWidth(),
                                    data = listOf(
                                        Point(0, 24.55),
                                        Point(entries.last().x + 1, 24.55),
                                    ),
                                    lineStyle = LineStyle(
                                        brush = SolidColor(MaterialTheme.colorScheme.secondary),
                                        strokeWidth = dimension.grid_0_5,
                                        pathEffect = null,
                                        alpha = 0.5f,
                                        colorFilter = null, // No color filter
                                        blendMode = DrawScope.DefaultBlendMode,
                                    ),
                                )
                            },
                        )
                    }
                }

                itemsIndexed(
                    items = uiState.rates,
                    key = { _, rate -> rate.validFrom.epochSeconds },
                ) { _, rate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimension.grid_2),
                    ) {
                        val timeLabel = rate.validFrom.toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(
                            modifier = Modifier.weight(1.0f),
                            text = "${timeLabel.date} ${timeLabel.time}",
                        )

                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            fontWeight = FontWeight.Bold,
                            text = "${rate.vatInclusivePrice}",
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()

        while (isActive) {
            delay(timeMillis = 1_800_000) // 30 minutes
            uiEvent.onRefresh()
        }
    }
}
