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

package com.rwmobi.kunigami.ui.composehelper

import androidx.compose.ui.graphics.Color

internal fun Color.darken(factor: Float): Color = Color(
    red = (this.red * factor).coerceIn(0f, 1f),
    green = (this.green * factor).coerceIn(0f, 1f),
    blue = (this.blue * factor).coerceIn(0f, 1f),
    alpha = this.alpha,
)

internal fun Color.luminance(): Float = 0.299f * red + 0.587f * green + 0.114f * blue

internal fun Color.getContrastColor(
    colorDark: Color = Color.Black,
    colorLight: Color = Color.White,
): Color {
    val luminance = luminance()
    return if (luminance > 0.5) colorDark else colorLight
}
