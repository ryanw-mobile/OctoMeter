/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.ui.components.DemoModeCtaAdaptive
import com.rwmobi.kunigami.ui.components.DualTitleBar
import com.rwmobi.kunigami.ui.components.ErrorScreenHandler
import com.rwmobi.kunigami.ui.components.LargeTitleWithIcon
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.composehelper.conditionalBlur
import com.rwmobi.kunigami.ui.destinations.agile.components.AgileTariffCardAdaptive
import com.rwmobi.kunigami.ui.destinations.agile.components.RateGroupCells
import com.rwmobi.kunigami.ui.destinations.agile.components.RateGroupTitle
import com.rwmobi.kunigami.ui.extensions.partitionList
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.model.chart.BarChartData
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.rate.RateGroupWithPartitions
import com.rwmobi.kunigami.ui.theme.cyanish
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.theme.purpleish
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.xygraph.HorizontalLineAnnotation
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_demo_introduction
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.agile_unit_rate_details
import kunigami.composeapp.generated.resources.agile_vat_unit_rate
import kunigami.composeapp.generated.resources.provide_api_key
import kunigami.composeapp.generated.resources.retail_region_unknown
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

    Box(modifier = modifier) {
        when {
            uiState.requestedScreenType is AgileScreenType.Error -> {
                ErrorScreenHandler(
                    modifier = Modifier.fillMaxSize(),
                    specialErrorScreen = uiState.requestedScreenType.specialErrorScreen,
                    onRefresh = {
                        uiEvent.onRefresh()
                    },
                )
            }

            uiState.requestedScreenType == AgileScreenType.Chart && uiState.rateGroupedCells.isNotEmpty() -> {
                // TODO: Refactor - move this to ViewModel and UIState
                // Pre-calculate the list of (rateGroup.title, partitionedItems)
                val rateGroupsWithPartitions = remember(uiState.rateGroupedCells, uiState.requestedRateColumns) {
                    uiState.rateGroupedCells.map { rateGroup ->
                        RateGroupWithPartitions(
                            title = rateGroup.title,
                            partitionedItems = rateGroup.rates.partitionList(columns = uiState.requestedRateColumns),
                        )
                    }
                }
                val shouldHideLastRateGroupColumn = remember(rateGroupsWithPartitions) {
                    rateGroupsWithPartitions.all {
                        it.partitionedItems.last().isEmpty()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .conditionalBlur(enabled = uiState.isLoading && uiState.barChartData == null),
                ) {
                    val subtitle = uiState.agileTariff?.let { primaryTariff ->
                        val regionCode = primaryTariff.getRetailRegion()?.stringResource
                            ?.let { stringResource(it) }
                            ?: stringResource(resource = Res.string.retail_region_unknown)

                        stringResource(resource = Res.string.agile_product_code_retail_region, primaryTariff.productCode, regionCode)
                    }

                    DualTitleBar(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.secondary)
                            .fillMaxWidth()
                            .height(height = dimension.minListItemHeight),
                        title = uiState.agileTariff?.displayName ?: "",
                        subtitle = subtitle,
                    )

                    ScrollbarMultiplatform(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.rateGroupedCells.isNotEmpty(),
                        lazyListState = lazyListState,
                    ) { contentModifier ->
                        LazyColumn(
                            modifier = contentModifier.conditionalBlur(enabled = uiState.isLoading),
                            contentPadding = PaddingValues(bottom = dimension.grid_4),
                            state = lazyListState,
                        ) {
                            if (uiState.isDemoMode == true) {
                                item(key = "demoCta") {
                                    DemoModeCtaAdaptive(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = dimension.grid_2),
                                        description = stringResource(resource = Res.string.agile_demo_introduction),
                                        ctaButtonLabel = stringResource(resource = Res.string.provide_api_key),
                                        onCtaButtonClicked = uiEvent.onNavigateToAccountTab,
                                        useWideLayout = uiState.requestedAdaptiveLayout != WindowWidthSizeClass.Compact,
                                    )
                                }
                            }

                            uiState.barChartData?.let { barChartData ->
                                renderChart(
                                    uiState = uiState,
                                    barChartData = barChartData,
                                )
                            }

                            item(key = "tariffDetails") {
                                AgileTariffCardAdaptive(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = dimension.grid_3,
                                            end = dimension.grid_3,
                                            top = dimension.grid_1,
                                        ),
                                    secondaryTariff = uiState.latestFixedTariff,
                                    rateRange = uiState.rateRange,
                                    rateGroupedCells = uiState.rateGroupedCells,
                                    requestedAdaptiveLayout = uiState.requestedAdaptiveLayout,
                                )
                            }

                            if (uiState.rateGroupedCells.isNotEmpty()) {
                                item(key = "headingUnitRateDetails") {
                                    Spacer(modifier = Modifier.height(height = dimension.grid_1))

                                    LargeTitleWithIcon(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = dimension.grid_2),
                                        icon = painterResource(resource = Res.drawable.revenue),
                                        label = stringResource(resource = Res.string.agile_unit_rate_details),
                                    )
                                }
                            }

                            rateGroupsWithPartitions.forEach { rateGroupsWithPartitions ->
                                item(key = "${rateGroupsWithPartitions.title}Title") {
                                    RateGroupTitle(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = dimension.grid_2,
                                                horizontal = dimension.grid_4,
                                            ),
                                        title = rateGroupsWithPartitions.title,
                                    )
                                }

                                val maxRows = rateGroupsWithPartitions.partitionedItems.maxOf { it.size }
                                items(maxRows) { rowIndex ->
                                    RateGroupCells(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = dimension.grid_4,
                                                vertical = dimension.grid_0_25,
                                            ),
                                        partitionedItems = rateGroupsWithPartitions.partitionedItems,
                                        shouldHideLastColumn = shouldHideLastRateGroupColumn,
                                        rateRange = uiState.rateRange,
                                        rowIndex = rowIndex,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            uiState.isLoading && uiState.barChartData == null -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                // No data - rarely when API request was successful but nothing returned
                ErrorScreenHandler(
                    modifier = Modifier.fillMaxSize(),
                    specialErrorScreen = SpecialErrorScreen.NoData,
                    onRefresh = uiEvent.onRefresh,
                )
            }
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()

        while (isActive) {
            val delayMillis = Clock.System.now().getNextHalfHourCountdownMillis()
            delay(timeMillis = delayMillis)
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

private fun LazyListScope.renderChart(
    uiState: AgileUIState,
    barChartData: BarChartData,
) {
    item(key = "chart") {
        val dimension = LocalDensity.current.getDimension()
        Box(
            modifier = Modifier.padding(top = dimension.grid_1),
        ) {
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
                showToolTipOnClick = uiState.showToolTipOnClick,
                entries = barChartData.verticalBarPlotEntries,
                yAxisRange = uiState.rateRange,
                yAxisTitle = stringResource(resource = Res.string.agile_vat_unit_rate),
                labelGenerator = { index ->
                    barChartData.labels[index]
                },
                tooltipGenerator = { index ->
                    barChartData.tooltips[index]
                },
                backgroundPlot = { graphScope ->
                    uiState.latestFixedTariff?.resolveUnitRate()?.let { fixedUnitRate ->
                        graphScope.HorizontalLineAnnotation(
                            location = fixedUnitRate,
                            lineStyle = LineStyle(
                                brush = SolidColor(purpleish),
                                strokeWidth = dimension.grid_0_25,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 16f), 0f),
                                alpha = 0.8f,
                                colorFilter = null, // No color filter
                                blendMode = DrawScope.DefaultBlendMode,
                            ),
                        )
                    }

                    uiState.latestFlexibleTariff?.resolveUnitRate()?.let { flexibleUnitRate ->
                        graphScope.HorizontalLineAnnotation(
                            location = flexibleUnitRate,
                            lineStyle = LineStyle(
                                brush = SolidColor(cyanish),
                                strokeWidth = dimension.grid_0_25,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 16f), 0f),
                                alpha = 0.8f,
                                colorFilter = null, // No color filter
                                blendMode = DrawScope.DefaultBlendMode,
                            ),
                        )
                    }
                },
            )
        }
    }
}
