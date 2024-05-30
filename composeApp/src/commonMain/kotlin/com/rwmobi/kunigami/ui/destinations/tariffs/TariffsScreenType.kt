/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs

import com.rwmobi.kunigami.ui.model.SpecialErrorScreen

sealed interface TariffsScreenType {
    data class ErrorScreen(val specialErrorScreen: SpecialErrorScreen) : TariffsScreenType
    data object TariffsList : TariffsScreenType
    data object FullScreenTariffsDetail : TariffsScreenType
}
