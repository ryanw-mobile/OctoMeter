/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.formatInstantWithoutSeconds
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.utils.generateGYRHueColorPalette
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.xygraph.HorizontalLineAnnotation
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
    val colorPalette = remember {
        generateGYRHueColorPalette(
            saturation = 0.6f,
            lightness = 0.6f,
        )
    }

    Box(modifier = modifier) {
        if (uiState.rates.isNotEmpty()) {
            val entries: List<VerticalBarPlotEntry<Int, Double>> = remember(uiState.rates) {
                buildList {
                    uiState.rates.forEachIndexed { index, rate ->
                        add(DefaultVerticalBarPlotEntry((index + 1), DefaultVerticalBarPosition(0.0, rate.vatInclusivePrice)))
                    }
                }
            }

            val labelIndex: Map<Int, Int> = remember(uiState.rates, uiState.requestedLayout) {
                buildMap {
                    // Generate all possible labels
                    var lastRateValue: Int? = null
                    uiState.rates.forEachIndexed { index, rate ->
                        val currentTime = rate.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
                        if (currentTime != lastRateValue) {
                            put(index + 1, currentTime)
                            lastRateValue = currentTime
                        }
                    }
                }
            }

            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxWidth(),
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

                            VerticalBarChart(
                                modifier = constraintModifier.padding(all = dimension.grid_2),
                                entries = entries,
                                yAxisRange = uiState.rateRange,
                                yAxisTickPosition = TickPosition.Outside,
                                xAxisTickPosition = TickPosition.Outside,
                                yAxisTitle = "VAT Unit Rate (p/kWh)",
                                xAxisTitle = "${uiState.rates.first().validFrom.formatInstantWithoutSeconds()} - ${uiState.rates.last().validTo?.formatInstantWithoutSeconds()}",
                                barWidth = 0.8f,
                                labelGenerator = { index ->
                                    labelIndex[index]?.toString()?.padStart(2, '0')
                                },
                                tooltipGenerator = { index ->
                                    with(uiState.rates[index]) {
                                        val timeRange = validFrom.toLocalHourMinuteString() +
                                            (validTo?.let { "- ${it.toLocalHourMinuteString()}" } ?: "")
                                        "$timeRange\n${vatInclusivePrice}p"
                                    }
                                },
                                colorPalette = colorPalette,
                                backgroundPlot = { graphScope ->
                                    graphScope.HorizontalLineAnnotation(
                                        location = 24.55,
                                        lineStyle = LineStyle(
                                            brush = SolidColor(MaterialTheme.colorScheme.error),
                                            strokeWidth = dimension.grid_0_5,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f),
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

        while (isActive) {
            delay(timeMillis = 1_800_000) // 30 minutes
            uiEvent.onRefresh()
        }
    }
}
