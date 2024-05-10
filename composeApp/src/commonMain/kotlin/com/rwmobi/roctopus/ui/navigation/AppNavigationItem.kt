/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.ui.navigation

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import roctopus.composeapp.generated.resources.Res
import roctopus.composeapp.generated.resources.navigation_account
import roctopus.composeapp.generated.resources.navigation_onboarding
import roctopus.composeapp.generated.resources.navigation_tariffs
import roctopus.composeapp.generated.resources.navigation_usage

@OptIn(ExperimentalResourceApi::class)
enum class AppNavigationItem(val titleResId: StringResource) {
    Onboarding(titleResId = Res.string.navigation_onboarding),
    Usage(titleResId = Res.string.navigation_usage),
    Tariffs(titleResId = Res.string.navigation_tariffs),
    Account(titleResId = Res.string.navigation_account),
    ;

    companion object {
        fun getNavBarItems(): List<AppNavigationItem> = listOf(Usage, Tariffs, Account)
    }
}
