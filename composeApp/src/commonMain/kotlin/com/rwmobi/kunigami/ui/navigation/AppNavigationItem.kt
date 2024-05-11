/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.navigation

import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.bar_chart
import kunigami.composeapp.generated.resources.coin
import kunigami.composeapp.generated.resources.compose_multiplatform
import kunigami.composeapp.generated.resources.navigation_account
import kunigami.composeapp.generated.resources.navigation_onboarding
import kunigami.composeapp.generated.resources.navigation_tariffs
import kunigami.composeapp.generated.resources.navigation_usage
import kunigami.composeapp.generated.resources.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

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
