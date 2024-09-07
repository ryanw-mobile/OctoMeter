/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.navigation

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
import org.jetbrains.compose.resources.StringResource

enum class AppDestination(val titleResId: StringResource, val iconResId: DrawableResource) {
    USAGE(titleResId = Res.string.navigation_usage, iconResId = Res.drawable.bar_chart),
    AGILE(titleResId = Res.string.navigation_agile, iconResId = Res.drawable.pulse),
    TARIFFS(titleResId = Res.string.navigation_tariffs, iconResId = Res.drawable.coin),
    ACCOUNT(titleResId = Res.string.navigation_account, iconResId = Res.drawable.user),
    ;

    companion object {
        fun getNavBarDestinations(): List<AppDestination> = listOf(AGILE, USAGE, TARIFFS, ACCOUNT)
        fun getStartDestination() = AGILE
    }
}
