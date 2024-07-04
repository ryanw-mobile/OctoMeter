/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.chart

import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class BarChartDataTest {
    // We intended to test BST in this case
    private val sampleRateList = listOf(
        Rate(
            vatInclusivePrice = 10.0,
            validity = Instant.parse("2024-07-04T10:00:00Z")..Instant.parse("2024-07-04T11:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = -5.0,
            validity = Instant.parse("2024-07-04T11:00:00Z")..Instant.parse("2024-07-04T12:00:00Z"),
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
        Rate(
            vatInclusivePrice = 15.0,
            validity = Instant.parse("2024-07-04T12:00:00Z")..Instant.DISTANT_FUTURE,
            paymentMethod = PaymentMethod.DIRECT_DEBIT,
        ),
    )

    @Test
    fun `fromRates should generate correct verticalBarPlotEntries`() = runBlocking {
        val rates = sampleRateList
        val expectedEntries = listOf(
            DefaultVerticalBarPlotEntry(
                x = 0,
                y = DefaultVerticalBarPosition(yMin = 0.0, yMax = 10.0),
            ),
            DefaultVerticalBarPlotEntry(
                x = 1,
                y = DefaultVerticalBarPosition(yMin = -5.0, yMax = 0.0),
            ),
            DefaultVerticalBarPlotEntry(
                x = 2,
                y = DefaultVerticalBarPosition(yMin = 0.0, yMax = 15.0),
            ),
        )

        val barChartData = BarChartData.fromRates(rates)

        assertEquals(expected = expectedEntries, actual = barChartData.verticalBarPlotEntries)
    }

    @Test
    fun `fromRates should generate correct labels`() = runBlocking {
        val rates = sampleRateList
        val expectedLabels = mapOf(
            0 to "11",
            1 to "12",
            2 to "13",
        )

        val barChartData = BarChartData.fromRates(rates)

        assertEquals(expected = expectedLabels, actual = barChartData.labels)
    }

    @Test
    fun `fromRates should generate correct tooltips`() = runBlocking {
        val rates = sampleRateList
        val expectedTooltips = listOf(
            "11:00 - 12:00\n10.0p",
            "12:00 - 13:00\n-5.0p",
            "13:00\n15.0p",
        )

        val barChartData = BarChartData.fromRates(rates)

        assertEquals(expected = expectedTooltips, actual = barChartData.tooltips)
    }
}
