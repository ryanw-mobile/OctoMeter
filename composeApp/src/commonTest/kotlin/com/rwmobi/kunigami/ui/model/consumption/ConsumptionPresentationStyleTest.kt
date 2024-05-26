/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup
import io.kotest.matchers.shouldBe
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.presentation_style_day_half_hourly
import kunigami.composeapp.generated.resources.presentation_style_month_thirty_days
import kunigami.composeapp.generated.resources.presentation_style_month_weeks
import kunigami.composeapp.generated.resources.presentation_style_week_seven_days
import kunigami.composeapp.generated.resources.presentation_style_year_twelve_months
import kotlin.test.Test

class ConsumptionPresentationStyleTest {

    @Test
    fun `DAY_HALF_HOURLY should map to HALF_HOURLY ConsumptionDataGroup`() {
        ConsumptionPresentationStyle.DAY_HALF_HOURLY.getConsumptionDataGroup() shouldBe ConsumptionDataGroup.HALF_HOURLY
    }

    @Test
    fun `WEEK_SEVEN_DAYS should map to DAY ConsumptionDataGroup`() {
        ConsumptionPresentationStyle.WEEK_SEVEN_DAYS.getConsumptionDataGroup() shouldBe ConsumptionDataGroup.DAY
    }

    @Test
    fun `MONTH_WEEKS should map to WEEK ConsumptionDataGroup`() {
        ConsumptionPresentationStyle.MONTH_WEEKS.getConsumptionDataGroup() shouldBe ConsumptionDataGroup.WEEK
    }

    @Test
    fun `MONTH_THIRTY_DAYS should map to DAY ConsumptionDataGroup`() {
        ConsumptionPresentationStyle.MONTH_THIRTY_DAYS.getConsumptionDataGroup() shouldBe ConsumptionDataGroup.DAY
    }

    @Test
    fun `YEAR_TWELVE_MONTHS should map to MONTH ConsumptionDataGroup`() {
        ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS.getConsumptionDataGroup() shouldBe ConsumptionDataGroup.MONTH
    }

    // Not a very good test, but we keep it for now.
    @Test
    fun `stringResource should match the expected resource`() {
        ConsumptionPresentationStyle.DAY_HALF_HOURLY.stringResource shouldBe Res.string.presentation_style_day_half_hourly
        ConsumptionPresentationStyle.WEEK_SEVEN_DAYS.stringResource shouldBe Res.string.presentation_style_week_seven_days
        ConsumptionPresentationStyle.MONTH_WEEKS.stringResource shouldBe Res.string.presentation_style_month_weeks
        ConsumptionPresentationStyle.MONTH_THIRTY_DAYS.stringResource shouldBe Res.string.presentation_style_month_thirty_days
        ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS.stringResource shouldBe Res.string.presentation_style_year_twelve_months
    }
}
