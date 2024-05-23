/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.components.LargeTitleWithIcon
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.model.RequestedChartLayout
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.utils.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.utils.getPercentageColorIndex
import com.rwmobi.kunigami.ui.utils.partitionList
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.HorizontalLineAnnotation
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_unit_rate_details
import kunigami.composeapp.generated.resources.agile_vat_unit_rate
import kunigami.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.rates.isNotEmpty(),
                lazyListState = lazyListState,
            ) { contentModifier ->
                LazyColumn(
                    modifier = contentModifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = dimension.grid_4),
                    state = lazyListState,
                ) {
                    uiState.barChartData?.let { barChartData ->
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
                                    entries = barChartData.verticalBarPlotEntries,
                                    yAxisRange = uiState.rateRange,
                                    yAxisTitle = stringResource(resource = Res.string.agile_vat_unit_rate),
                                    labelGenerator = { index ->
                                        barChartData.labels[index]
                                    },
                                    tooltipGenerator = { index ->
                                        barChartData.tooltips[index]
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
                    }

                    if (uiState.rates.isNotEmpty()) {
                        item(key = "headingUnitRateDetails") {
                            LargeTitleWithIcon(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimension.grid_2),
                                icon = painterResource(resource = Res.drawable.revenue),
                                label = stringResource(resource = Res.string.agile_unit_rate_details),
                            )
                        }
                    }

                    uiState.rates.forEach { rateGroup ->
                        item(key = "${rateGroup.title}Title") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = dimension.grid_2,
                                        horizontal = dimension.grid_4,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    text = rateGroup.title,
                                )

                                Text(
                                    modifier = Modifier.wrapContentSize(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    text = stringResource(resource = Res.string.agile_vat_unit_rate),
                                )
                            }
                        }

                        // We can do fancier grouping, but for now evenly-distributed is ok
                        val partitionedItems = rateGroup.rates.partitionList(columns = uiState.requestedRateColumns)
                        val maxRows = partitionedItems.maxOf { it.size }

                        items(maxRows) { rowIndex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = dimension.grid_4,
                                        vertical = dimension.grid_0_25,
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
                            ) {
                                for (columnIndex in partitionedItems.indices) {
                                    val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
                                    if (item != null) {
                                        IndicatorTextValueGridItem(
                                            modifier = Modifier.weight(1f),
                                            indicatorColor = colorPalette[
                                                item.vatInclusivePrice.getPercentageColorIndex(
                                                    maxValue = uiState.rateRange.endInclusive,
                                                ),
                                            ],
                                            label = item.validFrom.toLocalHourMinuteString(),
                                            value = item.vatInclusivePrice.roundToTwoDecimalPlaces().toString(precision = 2),
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
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

        while (isActive) {
            delay(timeMillis = 1_800_000) // 30 minutes
            uiEvent.onRefresh()
        }
    }

    LaunchedEffect(uiState.requestScrollToTop) {
        if (uiState.requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            uiEvent.onScrolledToTop()
        }
    }
}
