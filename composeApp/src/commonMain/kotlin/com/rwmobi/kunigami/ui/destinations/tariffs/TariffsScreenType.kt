/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen

@Immutable
sealed interface TariffsScreenType {
    data class Error(val specialErrorScreen: SpecialErrorScreen) : TariffsScreenType
    data object List : TariffsScreenType
    data object FullScreenDetail : TariffsScreenType
}
