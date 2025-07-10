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

package com.rwmobi.kunigami.domain.model.consumption

import androidx.compose.runtime.Immutable
import kotlin.math.ceil
import kotlin.time.DurationUnit
import kotlin.time.Instant

@Immutable
data class Consumption(
    val kWhConsumed: Double,
    val interval: ClosedRange<Instant>,
)

/**
 * Returns the number of days this consumption set spans across.
 * Note that an incomplete day with one record is counted as one whole day.
 */
fun List<Consumption>.getConsumptionDaySpan(): Int {
    val earliestStart = minOfOrNull { it.interval.start }
    val latestEnd = maxOfOrNull { it.interval.endInclusive }

    return if (earliestStart != null && latestEnd != null) {
        val duration = latestEnd - earliestStart
        val durationInDays = duration.toDouble(DurationUnit.DAYS)
        ceil(durationInDays).toInt()
    } else {
        0
    }
}

fun List<Consumption>.getConsumptionRange(): ClosedFloatingPointRange<Double> = if (isEmpty()) {
    0.0..0.0 // Return a default range if the list is empty
} else {
    0.0..ceil(maxOf { it.kWhConsumed } * 10) / 10.0
}
