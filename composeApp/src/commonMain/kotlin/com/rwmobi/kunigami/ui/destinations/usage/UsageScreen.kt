/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.components.LargeTitleWithIcon
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.MessageActionScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.composehelper.conditionalBlur
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.destinations.usage.components.RateGroupCells
import com.rwmobi.kunigami.ui.destinations.usage.components.RateGroupTitle
import com.rwmobi.kunigami.ui.destinations.usage.components.TariffProjectionsCardAdaptive
import com.rwmobi.kunigami.ui.destinations.usage.components.TitleNavigationBar
import com.rwmobi.kunigami.ui.extensions.partitionList
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.bolt
import kunigami.composeapp.generated.resources.error_screen_no_data_description_no_auth
import kunigami.composeapp.generated.resources.error_screen_no_data_title
import kunigami.composeapp.generated.resources.file_dotted
import kunigami.composeapp.generated.resources.kwh
import kunigami.composeapp.generated.resources.usage_energy_consumption_breakdown
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
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
        when {
            // We need to retain the navigation bar even for no data
            uiState.consumptionGroupedCells.isNotEmpty() || !uiState.isLoading -> {
                ScrollbarMultiplatform(
                    modifier = Modifier.fillMaxSize(),
                    enabled = uiState.consumptionGroupedCells.isNotEmpty(),
                    lazyListState = lazyListState,
                ) { contentModifier ->
                    LazyColumn(
                        modifier = contentModifier
                            .fillMaxSize()
                            .conditionalBlur(enabled = uiState.isLoading),
                        contentPadding = PaddingValues(bottom = dimension.grid_4),
                        state = lazyListState,
                    ) {
                        stickyHeader {
                            with(uiState.consumptionQueryFilter) {
                                TitleNavigationBar(
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colorScheme.secondary)
                                        .fillMaxWidth()
                                        .height(height = dimension.minListItemHeight),
                                    currentPresentationStyle = uiState.consumptionQueryFilter.presentationStyle,
                                    title = getConsumptionPeriodString(),
                                    canNavigateBack = uiState.consumptionQueryFilter.canNavigateBackward(accountMoveInDate = uiState.userProfile?.account?.movedInAt ?: Instant.DISTANT_PAST),
                                    canNavigateForward = uiState.consumptionQueryFilter.canNavigateForward(),
                                    onNavigateBack = uiEvent.onPreviousTimeFrame,
                                    onNavigateForward = uiEvent.onNextTimeFrame,
                                    onSwitchPresentationStyle = { uiEvent.onSwitchPresentationStyle(it) },
                                )
                            }
                        }

                        if (uiState.consumptionGroupedCells.isEmpty()) {
                            item(key = "noData") {
                                MessageActionScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    icon = painterResource(resource = Res.drawable.file_dotted),
                                    text = stringResource(resource = Res.string.error_screen_no_data_title),
                                    description = stringResource(resource = Res.string.error_screen_no_data_description_no_auth),
                                )
                            }
                        } else {
                            uiState.barChartData?.let { barChartData ->
                                item(key = "chart") {
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
                                            showToolTipOnClick = uiState.showToolTipOnClick,
                                            entries = barChartData.verticalBarPlotEntries,
                                            yAxisRange = uiState.consumptionRange,
                                            yAxisTitle = stringResource(resource = Res.string.kwh),
                                            colorPalette = colorPalette,
                                            labelGenerator = { index ->
                                                barChartData.labels[index]
                                            },
                                            tooltipGenerator = { index ->
                                                barChartData.tooltips[index]
                                            },
                                        )
                                    }
                                }
                            }

                            item(key = "tariffAndProjections") {
                                TariffProjectionsCardAdaptive(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = dimension.grid_3,
                                            vertical = dimension.grid_1,
                                        ),
                                    layoutType = uiState.requestedAdaptiveLayout,
                                    tariffSummary = uiState.userProfile?.tariffSummary,
                                    insights = uiState.insights,
                                    mpan = uiState.userProfile?.selectedMpan,
                                )
                            }

                            item(key = "headingConsumptionBreakdowns") {
                                LargeTitleWithIcon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(all = dimension.grid_2),
                                    icon = painterResource(resource = Res.drawable.bolt),
                                    label = stringResource(resource = Res.string.usage_energy_consumption_breakdown),
                                )
                            }

                            uiState.consumptionGroupedCells.forEach { consumptionGroup ->
                                item(key = "${consumptionGroup.title}Title") {
                                    RateGroupTitle(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = dimension.grid_2,
                                                horizontal = dimension.grid_4,
                                            ),
                                        consumptionGroup = consumptionGroup,
                                    )
                                }

                                // We can do fancier grouping, but for now evenly-distributed is ok
                                val partitionedItems = consumptionGroup.consumptions.partitionList(columns = uiState.requestedUsageColumns)
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
                                        rowIndex = rowIndex,
                                        maxInRange = uiState.consumptionRange.endInclusive,
                                        presentationStyle = uiState.consumptionQueryFilter.presentationStyle,
                                        colorPalette = colorPalette,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (uiState.isLoading) {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
    }

    LaunchedEffect(true) {
        uiEvent.onInitialLoad()
    }

    LaunchedEffect(uiState.consumptionGroupedCells) {
        lazyListState.scrollToItem(index = 0)
    }

    LaunchedEffect(uiState.requestScrollToTop) {
        if (uiState.requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            uiEvent.onScrolledToTop()
        }
    }
}
