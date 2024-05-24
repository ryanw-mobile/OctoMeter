/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model

import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.swap_fill
import kunigami.composeapp.generated.resources.trending_down_fill
import kunigami.composeapp.generated.resources.trending_up_fill
import org.jetbrains.compose.resources.DrawableResource

enum class RateTrend(drawableResource: DrawableResource) {
    UP(drawableResource = Res.drawable.trending_up_fill),
    DOWN(drawableResource = Res.drawable.swap_fill),
    STEADY(drawableResource = Res.drawable.trending_down_fill),
}
