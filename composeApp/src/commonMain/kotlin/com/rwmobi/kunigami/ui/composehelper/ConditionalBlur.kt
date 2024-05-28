/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.composehelper

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.conditionalBlur(
    enabled: Boolean,
    radius: Dp = 8.dp,
    edgeTreatment: BlurredEdgeTreatment = BlurredEdgeTreatment.Unbounded,
): Modifier {
    return if (enabled) {
        this.blur(
            radius = radius,
            edgeTreatment = edgeTreatment,
        )
    } else {
        this
    }
}
