/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup

@Immutable
enum class ConsumptionPresentationStyle {
    DAY_HALF_HOURLY,
    WEEK_SEVEN_DAYS,
    MONTH_WEEKS,
    MONTH_THIRTY_DAYS,
    YEAR_TWELVE_MONTHS,
    ;

    /***
     * UI uses interprets the grouping differently. This is to map to the Enum that the API expects.
     */
    fun getConsumptionDataGroup(): ConsumptionDataGroup {
        return when (this) {
            DAY_HALF_HOURLY -> ConsumptionDataGroup.HALF_HOURLY
            WEEK_SEVEN_DAYS -> ConsumptionDataGroup.DAY
            MONTH_WEEKS -> ConsumptionDataGroup.WEEK
            MONTH_THIRTY_DAYS -> ConsumptionDataGroup.DAY
            YEAR_TWELVE_MONTHS -> ConsumptionDataGroup.MONTH
        }
    }
}
