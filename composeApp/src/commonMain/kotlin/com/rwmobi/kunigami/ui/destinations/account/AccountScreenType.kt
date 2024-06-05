/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import com.rwmobi.kunigami.ui.model.SpecialErrorScreen

sealed interface AccountScreenType {
    data class Error(val specialErrorScreen: SpecialErrorScreen) : AccountScreenType
    data object Onboarding : AccountScreenType
    data object Account : AccountScreenType
}
