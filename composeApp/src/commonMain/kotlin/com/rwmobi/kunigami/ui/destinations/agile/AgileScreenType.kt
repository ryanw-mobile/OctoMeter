/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen

@Immutable
sealed interface AgileScreenType {
    data class Error(val specialErrorScreen: SpecialErrorScreen) : AgileScreenType
    data object Chart : AgileScreenType
}
