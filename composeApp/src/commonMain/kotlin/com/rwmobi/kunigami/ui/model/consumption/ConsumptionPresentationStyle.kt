/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.consumption

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.presentation_style_day_half_hourly
import kunigami.composeapp.generated.resources.presentation_style_month_thirty_days
import kunigami.composeapp.generated.resources.presentation_style_month_weeks
import kunigami.composeapp.generated.resources.presentation_style_week_seven_days
import kunigami.composeapp.generated.resources.presentation_style_year_twelve_months
import org.jetbrains.compose.resources.StringResource

@Immutable
enum class ConsumptionPresentationStyle(val stringResource: StringResource) {
    DAY_HALF_HOURLY(stringResource = Res.string.presentation_style_day_half_hourly),
    WEEK_SEVEN_DAYS(stringResource = Res.string.presentation_style_week_seven_days),
    MONTH_WEEKS(stringResource = Res.string.presentation_style_month_weeks),
    MONTH_THIRTY_DAYS(stringResource = Res.string.presentation_style_month_thirty_days),
    YEAR_TWELVE_MONTHS(stringResource = Res.string.presentation_style_year_twelve_months),
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
