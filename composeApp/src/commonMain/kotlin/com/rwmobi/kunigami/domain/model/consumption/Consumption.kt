/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.consumption

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlin.math.ceil
import kotlin.time.DurationUnit

@Immutable
data class Consumption(
    val consumption: Double,
    val intervalStart: Instant,
    val intervalEnd: Instant,
)

/**
 * Returns the time span of this consumption set.
 * Note that an incomplete day with one record is counted as one whole day.
 */
fun List<Consumption>.getConsumptionTimeSpan(): Int {
    val earliestStart = minOfOrNull { it.intervalStart }
    val latestEnd = maxOfOrNull { it.intervalEnd }

    return if (earliestStart != null && latestEnd != null) {
        val duration = latestEnd - earliestStart
        val durationInDays = duration.toDouble(DurationUnit.DAYS)
        kotlin.math.ceil(durationInDays).toInt()
    } else {
        0
    }
}

fun List<Consumption>.getRange(): ClosedFloatingPointRange<Double> {
    return if (isEmpty()) {
        0.0..0.0 // Return a default range if the list is empty
    } else {
        0.0..ceil(maxOf { it.consumption } * 10) / 10.0
    }
}
