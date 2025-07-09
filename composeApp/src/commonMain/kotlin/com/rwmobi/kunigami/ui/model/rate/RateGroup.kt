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

package com.rwmobi.kunigami.ui.model.rate

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlin.time.Duration
import kotlin.time.Instant

@Immutable
data class RateGroup(
    val title: String,
    val rates: List<Rate>,
)

fun List<RateGroup>.findActiveRate(referencePoint: Instant): Rate? {
    this.forEach { group ->
        val activeRate = group.rates.find { rate ->
            rate.isActive(referencePoint)
        }
        if (activeRate != null) {
            return activeRate
        }
    }
    return null
}

fun List<RateGroup>.getRateTrend(activeRate: Rate?): RateTrend? = activeRate?.let {
    if (it.validity.endInclusive == Instant.DISTANT_FUTURE) {
        RateTrend.STEADY
    } else {
        val nextRate = findActiveRate(referencePoint = it.validity.endInclusive.plus(Duration.parse("5m")))

        when {
            nextRate == null -> null
            it.vatInclusivePrice > nextRate.vatInclusivePrice -> RateTrend.DOWN
            it.vatInclusivePrice == nextRate.vatInclusivePrice -> RateTrend.STEADY
            else -> RateTrend.UP
        }
    }
}
