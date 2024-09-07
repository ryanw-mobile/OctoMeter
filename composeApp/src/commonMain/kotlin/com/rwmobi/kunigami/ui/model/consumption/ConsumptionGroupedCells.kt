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
