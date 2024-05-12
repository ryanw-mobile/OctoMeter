/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import io.github.koalaplot.core.xygraph.TickPosition
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisStyle

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

    if (!uiState.isLoading) {
        val entries: List<VerticalBarPlotEntry<Int, Double>> = buildList {
            uiState.consumptions.forEachIndexed { index, consumption ->
                add(DefaultVerticalBarPlotEntry((index + 1), DefaultVerticalBarPosition(0.0, consumption.consumption)))
                Logger.d("Adding ${index + 1}, ${consumption.consumption}")
            }
        }

        BarSamplePlot(
            modifier = Modifier.fillMaxSize(),
            entries = entries,
            thumbnail = false,
            tickPositionState = TickPositionState(verticalAxis = TickPosition.None, horizontalAxis = TickPosition.None),
            title = "Consumption",
        )
//        ScrollbarMultiplatform(
//            modifier = modifier,
//            enabled = uiState.consumptions.isNotEmpty(),
//            lazyListState = lazyListState,
//        ) { contentModifier ->
//            LazyColumn(
//                modifier = contentModifier.fillMaxSize(),
//                state = lazyListState,
//            ) {
//                itemsIndexed(
//                    items = uiState.consumptions,
//                    key = { _, consumption -> consumption.intervalStart },
//                ) { _, consumption ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = dimension.grid_2),
//                    ) {
//                        val timeLabel = consumption.intervalStart.toLocalDateTime(TimeZone.currentSystemDefault())
//                        Text(
//                            modifier = Modifier.weight(1.0f),
//                            text = "${timeLabel.date} ${timeLabel.time}",
//                        )
//
//                        Text(
//                            modifier = Modifier.wrapContentWidth(),
//                            fontWeight = FontWeight.Bold,
//                            text = "${consumption.consumption}",
//                        )
//                    }
//                }
//            }
//        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun BarSamplePlot(
    entries: List<VerticalBarPlotEntry<Int, Double>>,
    modifier: Modifier,
    thumbnail: Boolean = false,
    tickPositionState: TickPositionState,
    title: String,
) {
    val barChartEntries = remember(thumbnail) { entries }

    ChartLayout(
        modifier = modifier,
        title = {
            ChartTitle(title)
        },
    ) {
        XYGraph(
            xAxisModel = IntLinearAxisModel(
                XAxisRange,
                minimumMajorTickIncrement = 1,
                minimumMajorTickSpacing = 10.dp,
                zoomRangeLimit = 3,
                minorTickCount = 0,
            ),
            yAxisModel = DoubleLinearAxisModel(
                YAxisRange,
                minimumMajorTickIncrement = 0.1,
                minorTickCount = 0,
            ),
            xAxisStyle = rememberAxisStyle(
                tickPosition = tickPositionState.horizontalAxis,
                color = Color.LightGray,
            ),
            xAxisLabels = {
                if (!thumbnail) {
                    AxisLabel("$it", Modifier.padding(top = 2.dp))
                }
            },
            xAxisTitle = { if (!thumbnail) AxisTitle("Position in Sequence") },
            yAxisStyle = rememberAxisStyle(tickPosition = tickPositionState.verticalAxis),
            yAxisLabels = {
                if (!thumbnail) AxisLabel(it.toString(1), Modifier.absolutePadding(right = 2.dp))
            },
            yAxisTitle = {
                if (!thumbnail) {
                    AxisTitle(
                        "Value",
                        modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                            .padding(bottom = padding),
                    )
                }
            },
            verticalMajorGridLineStyle = null,
        ) {
            VerticalBarPlot(
                barChartEntries,
                bar = { index ->
                    DefaultVerticalBar(
                        brush = SolidColor(colors[0]),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (!thumbnail) {
                            HoverSurface { Text(barChartEntries[index].y.yMax.toString()) }
                        }
                    }
                },
                barWidth = BarWidth,
            )
        }
    }
}

private val colors = generateHueColorPalette(80)
private const val BarWidth = 0.8f

private data class TickPositionState(
    val verticalAxis: TickPosition,
    val horizontalAxis: TickPosition,
)

private val YAxisRange = 0.0..1.0
private val XAxisRange = 0..66

@Composable
private fun TickPositionSelector(
    state: TickPositionState,
    update: (TickPositionState) -> Unit,
) {
    ExpandableCard(
        modifier = paddingMod,
        titleContent = { Text("Axis options", modifier = paddingMod) },
    ) {
        Row {
            Column {
                Text("Vertical")
                TickPosition.entries.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            it == state.verticalAxis,
                            onClick = { update(state.copy(verticalAxis = it)) },
                        )
                        Text(it.name)
                    }
                }
            }
            Column {
                Text("Horizontal")
                TickPosition.entries.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            it == state.horizontalAxis,
                            onClick = { update(state.copy(horizontalAxis = it)) },
                        )
                        Text(it.name)
                    }
                }
            }
        }
    }
}

@Composable
fun ChartTitle(title: String) {
    Column {
        Text(
            title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun AxisTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
    )
}

@Composable
fun AxisLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
fun HoverSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.LightGray,
        modifier = modifier.padding(padding),
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}

internal val padding = 8.dp
internal val paddingMod = Modifier.padding(padding)
private const val DegreesHalfCircle: Float = 180.0f

@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: CardColors = CardDefaults.cardColors(),
    border: BorderStroke? = null,
    elevation: CardElevation = CardDefaults.cardElevation(),
    initExpandedState: Boolean = false,
    titleContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var expandedState by remember { mutableStateOf(initExpandedState) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) DegreesHalfCircle else 0f,
    )

    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                titleContent.invoke()
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    },
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop-Down Arrow",
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            ) {
                if (expandedState) {
                    content()
                }
            }
        }
    }
}
