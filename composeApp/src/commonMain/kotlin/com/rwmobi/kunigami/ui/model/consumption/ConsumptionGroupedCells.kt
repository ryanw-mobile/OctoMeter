/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.consumption

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.extensions.roundToNearestEvenHundredth
import com.rwmobi.kunigami.domain.model.consumption.Consumption

@Immutable
data class ConsumptionGroupedCells(
    val title: String,
    val consumptions: List<Consumption>,
) {
    /**
     * Returns the sum of consumptions without rounding.
     * For billing purpose, it should apply roundToNearestEvenHundredth(),
     * but this rounding introduces error. If it does not match the exact billing period,
     * either way will not produce the exact the billed amount.
     */
    fun getAggregateConsumption(): Double {
        return consumptions.sumOf { it.kWhConsumed }
    }
}

/**
 * Returns the sum of consumptions with rounding, for billing purpose.
 * This rounding introduces error. If it does not match the exact billing period,
 * this will not match exactly the billable amount.
 */
fun List<ConsumptionGroupedCells>.getAggregateConsumption(rounded: Boolean): Double {
    return sumOf { it.getAggregateConsumption() }.run {
        if (rounded) {
            roundToNearestEvenHundredth()
        } else {
            this
        }
    }
}
