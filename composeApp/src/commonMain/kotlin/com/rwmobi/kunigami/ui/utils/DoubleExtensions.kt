/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

internal fun Double.getPercentageColorIndex(maxValue: Double): Int {
    return ((this / maxValue) * 100).toInt().coerceIn(0, 99)
}
