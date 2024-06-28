/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionQueryFilter

@Immutable
data class UsageUIEvent(
    val onInitialLoad: () -> Unit,
    val onPreviousTimeFrame: (consumptionQueryFilter: ConsumptionQueryFilter) -> Unit,
    val onNextTimeFrame: (consumptionQueryFilter: ConsumptionQueryFilter) -> Unit,
    val onSwitchPresentationStyle: (consumptionQueryFilter: ConsumptionQueryFilter, presentationStyle: ConsumptionPresentationStyle) -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onNavigateToAccountTab: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
