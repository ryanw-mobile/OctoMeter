/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package com.rwmobi.kunigami.ui.components.koalaplot

import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.TickPosition
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import kotlin.math.min

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun BarSamplePlot(
    modifier: Modifier,
    entries: List<VerticalBarPlotEntry<Int, Double>>,
    labels: Map<Int, String>,
    yAxisRange: ClosedFloatingPointRange<Double>,
    yAxisTickPosition: TickPosition,
    xAxisTickPosition: TickPosition,
    title: String? = null,
    xAxisTitle: String? = null,
    yAxisTitle: String? = null,
    barWidth: Float,
) {
    val dimension = LocalDensity.current.getDimension()
    val barChartEntries = remember { entries }
    val colorPalette = remember {
        generateGYRHueColorPalette(
            saturation = 0.6f,
            lightness = 0.6f,
        )
    }

    ChartLayout(
        modifier = modifier,
        title = {
            title?.let {
                ChartTitle(title = title)
            }
        },
    ) {
        XYGraph(
            xAxisModel = IntLinearAxisModel(
                range = 0..entries.count() + 1,
                minimumMajorTickIncrement = 1,
                minimumMajorTickSpacing = dimension.grid_1,
                minorTickCount = 0,
                allowPanning = false,
                allowZooming = false,
            ),
            yAxisModel = DoubleLinearAxisModel(
                range = yAxisRange,
                minimumMajorTickIncrement = 0.1,
                minorTickCount = 4,
                allowZooming = false,
                allowPanning = false,
            ),
            xAxisStyle = rememberAxisStyle(
                tickPosition = xAxisTickPosition,
            ),
            xAxisLabels = {
                labels[it]?.let { label ->
                    AxisLabel(
                        modifier = Modifier
                            .padding(top = 2.dp),
                        label = label,
                    )
                }
            },
            xAxisTitle = {
                xAxisTitle?.let {
                    XAxisTitle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimension.grid_1),
                        title = it,
                    )
                }
            },
            yAxisStyle = rememberAxisStyle(
                tickPosition = yAxisTickPosition,
            ),
            yAxisLabels = {
                AxisLabel(
                    modifier = Modifier.absolutePadding(right = 2.dp),
                    label = it.toString(1),
                )
            },
            yAxisTitle = {
                yAxisTitle?.let {
                    YAxisTitle(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = dimension.grid_1),
                        title = it,
                    )
                }
            },
            verticalMajorGridLineStyle = null,
            horizontalMajorGridLineStyle = LineStyle(
                brush = SolidColor(MaterialTheme.colorScheme.onBackground), // Set the color of the line
                strokeWidth = 1.dp, // Set the thickness of the line
                pathEffect = null,
                alpha = 0.5f, // Opacity of the line
                colorFilter = null, // No color filter
                blendMode = DrawScope.DefaultBlendMode, // Default blending mode
            ),
            horizontalMinorGridLineStyle = LineStyle(
                brush = SolidColor(MaterialTheme.colorScheme.onBackground), // Set the color of the line
                strokeWidth = 1.dp, // Set the thickness of the line
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f), // Configure dashed pattern
                alpha = 0.25f, // Opacity of the line
                colorFilter = null, // No color filter
                blendMode = DrawScope.DefaultBlendMode, // Default blending mode
            ),
        ) {
            LinePlot(
                modifier = Modifier.fillMaxWidth(),
                data = listOf(
                    Point(0, 24.55),
                    Point(entries.last().x+1, 24.55),
                ),
                lineStyle = LineStyle(
                    brush = SolidColor(MaterialTheme.colorScheme.secondary), // Set the color of the line
                    strokeWidth = 4.dp, // Set the thickness of the line
                    pathEffect = null,
                    alpha = 0.5f, // Opacity of the line
                    colorFilter = null, // No color filter
                    blendMode = DrawScope.DefaultBlendMode, // Default blending mode
                ),
            )

            VerticalBarPlot(
                data = barChartEntries,
                bar = { index ->
                    DefaultVerticalBar(
                        modifier = Modifier.fillMaxWidth()
//                            .pointerInput(Unit) {
//                                detectTapGestures(
//                                    onPress = { /* Called when the press is detected */ },
//                                    onDoubleTap = { /* Called on double tap */ },
//                                    onLongPress = { /* Called on long press */ },
//                                    onTap = {
//
//                                        // Handle the tap
//                                        if (!thumbnail) {
//                                            HoverSurface { Text(barChartEntries[index].y.yMax.toString()) }
//                                        }
//                                    },
//                                )
//                            }
                            .hoverableElement {
                                //   HoverSurface(padding = dimension.grid_1) {
                                RichTooltip {
                                    Text(
                                        text = "${labels[index]}\n${barChartEntries[index].y.yMax}",
                                    )
                                }
                                //   }
                            },
                        brush = SolidColor(
                            colorPalette[
                                getPercentageColorIndex(
                                    value = barChartEntries[index].y.yMax,
                                    maxValue = yAxisRange.endInclusive,
                                ),
                            ],
                        ),
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                        ),
                    )
                },
                barWidth = barWidth,
            )
        }
    }
}

private fun getPercentageColorIndex(value: Double, maxValue: Double): Int {
    return min(((value / maxValue) * 100).toInt() - 1, 99)
}

private fun generateGYRHueColorPalette(
    saturation: Float = 0.5f,
    lightness: Float = 0.5f,
): List<Color> {
    val count = 100
    val startHue = 120f // Starting at green
    val endHue = 0f // Ending at red
    val delta = (endHue - startHue) / (count - 1) // Calculate delta for exactly 100 steps

    return List(count) { i ->
        val hue = startHue + delta * i // Compute the hue for this index
        Color.hsl(hue, saturation, lightness)
    }
}
