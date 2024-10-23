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

package com.rwmobi.kunigami.ui.components.koalaplot

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.composehelper.palette.RatePalette
import com.rwmobi.kunigami.ui.composehelper.shouldUseDarkTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.AxisStyle
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.TickPosition
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.XYGraphScope
import io.github.koalaplot.core.xygraph.rememberAxisStyle

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun VerticalBarChart(
    modifier: Modifier,
    showToolTipOnClick: Boolean,
    entries: List<VerticalBarPlotEntry<Int, Double>>,
    yAxisRange: ClosedFloatingPointRange<Double>,
    labelGenerator: (index: Int) -> String?,
    tooltipGenerator: (index: Int) -> String,
    xAxisTitle: String? = null,
    yAxisTitle: String? = null,
    backgroundPlot: @Composable ((scope: XYGraphScope<Int, Double>) -> Unit)? = null,
) {
    val dimension = getScreenSizeInfo().getDimension()

    Box {
        var showTooltipIndex: Int? by remember { mutableStateOf(null) }
        ChartLayout(modifier = modifier) {
            XYGraph(
                panZoomEnabled = false,
                xAxisModel = CategoryAxisModel(
                    categories = entries.indices.toList(),
                    firstCategoryIsZero = false, // true means first column cut into half
                ),
                xAxisLabels = { index ->
                    labelGenerator(index)?.let { label ->
                        AxisLabel(
                            modifier = Modifier.padding(horizontal = dimension.grid_1),
                            label = label,
                        )
                    }
                },
                xAxisStyle = AxisStyle(
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.25f,
                    ),
                    majorTickSize = 4.dp,
                    tickPosition = TickPosition.Outside,
                    labelRotation = 90,
                ),
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
                verticalMajorGridLineStyle = null,
                verticalMinorGridLineStyle = null,

                yAxisModel = DoubleLinearAxisModel(
                    range = yAxisRange,
                    minimumMajorTickIncrement = 0.1,
                    minorTickCount = 4,
                    allowZooming = false,
                    allowPanning = false,
                ),
                yAxisStyle = rememberAxisStyle(
                    tickPosition = TickPosition.Outside,
                ),
                yAxisLabels = {
                    AxisLabel(
                        modifier = Modifier.absolutePadding(right = dimension.grid_0_25),
                        label = it.toString(precision = 1),
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
                horizontalMajorGridLineStyle = LineStyle(
                    brush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    strokeWidth = 1.dp,
                    pathEffect = null,
                    alpha = 0.5f,
                    colorFilter = null,
                    blendMode = DrawScope.DefaultBlendMode,
                ),
                horizontalMinorGridLineStyle = LineStyle(
                    brush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    strokeWidth = 1.dp,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f), // Configure dashed pattern
                    alpha = 0.25f,
                    colorFilter = null,
                    blendMode = DrawScope.DefaultBlendMode,
                ),
            ) {
                backgroundPlot?.let { it(this) }

                VerticalBarPlot(
                    data = entries,
                    barWidth = 0.8f,
                    bar = { index ->
                        DefaultVerticalBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    if (showToolTipOnClick) {
                                        detectTapGestures(
                                            onPress = { _ ->
                                                showTooltipIndex = index
                                                try {
                                                    awaitRelease()
                                                } finally {
                                                    if (showTooltipIndex == index) {
                                                        showTooltipIndex = null
                                                    }
                                                }
                                            },
                                        )
                                    }
                                },
                            brush = SolidColor(
                                RatePalette.lookupColorFromRange(
                                    value = if (entries[index].y.yMin >= 0) entries[index].y.yMax else entries[index].y.yMin,
                                    range = yAxisRange,
                                    shouldUseDarkTheme = shouldUseDarkTheme(),
                                ),
                            ),
                            shape = if (entries[index].y.yMin >= 0) {
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 0.dp,
                                )
                            } else {
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 8.dp,
                                    bottomStart = 8.dp,
                                )
                            },
                            hoverElement = {
                                HoverElement(text = tooltipGenerator(index))
                            },
                            border = null,
                        )
                    },
                )
            }
        }

        showTooltipIndex?.let { index ->
            HoverElement(
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(y = dimension.grid_2),
                text = tooltipGenerator(index),
            )
        }
    }
}

@Composable
private fun HoverElement(
    modifier: Modifier = Modifier,
    text: String,
) {
    val dimension = getScreenSizeInfo().getDimension()

    Text(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.inverseSurface)
            .padding(
                horizontal = dimension.grid_2,
                vertical = dimension.grid_1,
            ),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.inverseOnSurface,
        textAlign = TextAlign.Center,
        text = text,
    )
}

@Composable
@Preview
private fun HoverElementPreview() {
    CommonPreviewSetup {
        HoverElement(
            text = "10:00 - 10:30\n12.24p",
        )
    }
}
