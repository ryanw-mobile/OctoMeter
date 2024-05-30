/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.destinations.agile.components.AgileTariffCardAdaptive
import com.rwmobi.kunigami.ui.destinations.agile.components.RateGroupCells
import com.rwmobi.kunigami.ui.destinations.agile.components.RateGroupTitle
import com.rwmobi.kunigami.ui.extensions.partitionList
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.theme.getDimension
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
import kunigami.composeapp.generated.resources.revenue
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
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
                ScrollbarMultiplatform(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.rateGroupedCells.isNotEmpty(),
                    lazyListState = lazyListState,
                ) { contentModifier ->
                    LazyColumn(
                        modifier = contentModifier
                            .fillMaxSize()
                            .conditionalBlur(enabled = uiState.isLoading && uiState.barChartData == null),
                        contentPadding = PaddingValues(bottom = dimension.grid_4),
                        state = lazyListState,
                    ) {
                        stickyHeader(key = "header") {
                            val subtitle = uiState.agileTariffSummary?.let {
                                val regionCode = it.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
                                stringResource(resource = Res.string.agile_product_code_retail_region, it.productCode, regionCode)
                            }
                            DualTitleBar(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .fillMaxWidth()
                                    .height(height = dimension.minListItemHeight),
                                title = uiState.agileTariffSummary?.displayName ?: "",
                                subtitle = subtitle,
                            )
                        }

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
                            item(key = "chart") {
                                BoxWithConstraints(
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
                                        colorPalette = colorPalette,
                                        backgroundPlot = { graphScope ->
                                            if (uiState.isOnDifferentTariff() &&
                                                uiState.userProfile?.tariffSummary != null
                                            ) {
                                                graphScope.HorizontalLineAnnotation(
                                                    location = uiState.userProfile.tariffSummary.vatInclusiveUnitRate,
                                                    lineStyle = LineStyle(
                                                        brush = SolidColor(MaterialTheme.colorScheme.error),
                                                        strokeWidth = dimension.grid_0_5,
                                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f),
                                                        alpha = 0.5f,
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

                        item(key = "tariffDetails") {
                            AgileTariffCardAdaptive(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = dimension.grid_3,
                                        end = dimension.grid_3,
                                        top = dimension.grid_1,
                                    ),
                                agileTariffSummary = uiState.agileTariffSummary,
                                differentTariffSummary = uiState.userProfile?.tariffSummary,
                                colorPalette = colorPalette,
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

                        uiState.rateGroupedCells.forEach { rateGroup ->
                            item(key = "${rateGroup.title}Title") {
                                RateGroupTitle(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = dimension.grid_2,
                                            horizontal = dimension.grid_4,
                                        ),
                                    title = rateGroup.title,
                                )
                            }

                            // We can do fancier grouping, but for now evenly-distributed is ok
                            val partitionedItems = rateGroup.rates.partitionList(columns = uiState.requestedRateColumns)
                            val maxRows = partitionedItems.maxOf { it.size }

                            items(maxRows) { rowIndex ->
                                RateGroupCells(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = dimension.grid_4,
                                            vertical = dimension.grid_0_25,
                                        ),
                                    partitionedItems = partitionedItems,
                                    maxInRange = uiState.rateRange.endInclusive,
                                    rowIndex = rowIndex,
                                    colorPalette = colorPalette,
                                )
                            }
                        }
                    }
                }
            }

            uiState.isLoading -> {
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
