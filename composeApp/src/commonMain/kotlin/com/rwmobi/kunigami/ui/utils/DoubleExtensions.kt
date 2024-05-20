/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import kotlin.math.min

internal fun Double.getPercentageColorIndex(maxValue: Double): Int {
    return min(((this / maxValue) * 100).toInt() - 1, 99)
}
