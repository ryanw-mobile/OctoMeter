/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.navigation

import androidx.compose.runtime.Immutable
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.bar_chart
import kunigami.composeapp.generated.resources.coin
import kunigami.composeapp.generated.resources.navigation_account
import kunigami.composeapp.generated.resources.navigation_agile
import kunigami.composeapp.generated.resources.navigation_tariffs
import kunigami.composeapp.generated.resources.navigation_usage
import kunigami.composeapp.generated.resources.pulse
import kunigami.composeapp.generated.resources.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@Immutable
@OptIn(ExperimentalResourceApi::class)
enum class AppNavigationItem(val titleResId: StringResource, val iconResId: DrawableResource) {
    USAGE(titleResId = Res.string.navigation_usage, iconResId = Res.drawable.bar_chart),
    AGILE(titleResId = Res.string.navigation_agile, iconResId = Res.drawable.pulse),
    TARIFFS(titleResId = Res.string.navigation_tariffs, iconResId = Res.drawable.coin),
    ACCOUNT(titleResId = Res.string.navigation_account, iconResId = Res.drawable.user),
    ;

    companion object {
        fun getNavBarItems(): List<AppNavigationItem> = listOf(USAGE, AGILE, TARIFFS, ACCOUNT)
    }
}
