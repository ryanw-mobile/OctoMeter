/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.ui.navigation

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import roctopus.composeapp.generated.resources.Res
import roctopus.composeapp.generated.resources.bar_chart
import roctopus.composeapp.generated.resources.coin
import roctopus.composeapp.generated.resources.compose_multiplatform
import roctopus.composeapp.generated.resources.navigation_account
import roctopus.composeapp.generated.resources.navigation_onboarding
import roctopus.composeapp.generated.resources.navigation_tariffs
import roctopus.composeapp.generated.resources.navigation_usage
import roctopus.composeapp.generated.resources.user

@OptIn(ExperimentalResourceApi::class)
enum class AppNavigationItem(val titleResId: StringResource, val iconResId: DrawableResource) {
    Onboarding(titleResId = Res.string.navigation_onboarding, iconResId = Res.drawable.compose_multiplatform),
    Usage(titleResId = Res.string.navigation_usage, iconResId = Res.drawable.bar_chart),
    Tariffs(titleResId = Res.string.navigation_tariffs, iconResId = Res.drawable.coin),
    Account(titleResId = Res.string.navigation_account, iconResId = Res.drawable.user),
    ;

    companion object {
        fun getNavBarItems(): List<AppNavigationItem> = listOf(Usage, Tariffs, Account)
    }
}
