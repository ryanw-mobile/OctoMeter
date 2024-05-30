/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import com.rwmobi.kunigami.ui.model.SpecialErrorScreen

sealed interface TariffsScreenType {
    data class Error(val specialErrorScreen: SpecialErrorScreen) : TariffsScreenType
    data object List : TariffsScreenType
    data object FullScreenDetail : TariffsScreenType
}
