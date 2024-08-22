/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.chart

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.tools.MultiplatformStringResourceProvider
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
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

    // 🗂 fromRates
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

        val barChartData = BarChartData.fromRates(
            rates = rates,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

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

        val barChartData = BarChartData.fromRates(
            rates = rates,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

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

        val barChartData = BarChartData.fromRates(
            rates = rates,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedTooltips, actual = barChartData.tooltips)
    }

    // 🗂 fromConsumptions
    // Helper function to create Consumption objects for testing
    private fun createConsumption(kWhConsumed: Double, start: String, end: String): Consumption {
        return Consumption(
            kWhConsumed = kWhConsumed,
            interval = Instant.parse(start)..Instant.parse(end),
        )
    }

    @Test
    fun `fromConsumptions should generate correct verticalBarPlotEntries`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-07-04T10:00:00Z", end = "2024-07-04T10:30:00Z"),
            createConsumption(kWhConsumed = 10.0, start = "2024-07-04T10:30:00Z", end = "2024-07-04T11:00:00Z"),
            createConsumption(kWhConsumed = 15.0, start = "2024-07-04T11:00:00Z", end = "2024-07-04T11:30:00Z"),
        )
        val expectedEntries = listOf(
            DefaultVerticalBarPlotEntry(
                x = 0,
                y = DefaultVerticalBarPosition(yMin = 0.0, yMax = 5.0),
            ),
            DefaultVerticalBarPlotEntry(
                x = 1,
                y = DefaultVerticalBarPosition(yMin = 0.0, yMax = 10.0),
            ),
            DefaultVerticalBarPlotEntry(
                x = 2,
                y = DefaultVerticalBarPosition(yMin = 0.0, yMax = 15.0),
            ),
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expectedEntries, barChartData.verticalBarPlotEntries)
    }

    @Test
    fun `fromConsumptions should generate correct labels for DAY_HALF_HOURLY`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-07-04T01:00:00Z", end = "2024-07-04T01:30:00Z"),
            createConsumption(kWhConsumed = 10.0, start = "2024-07-04T01:30:00Z", end = "2024-07-04T02:00:00Z"),
            createConsumption(kWhConsumed = 15.0, start = "2024-07-04T02:00:00Z", end = "2024-07-04T02:30:00Z"),
            createConsumption(kWhConsumed = 20.0, start = "2024-07-04T02:30:00Z", end = "2024-07-04T03:00:00Z"),
        )
        val expectedLabels = mapOf(
            0 to "02",
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedLabels, actual = barChartData.labels)
    }

    @Test
    fun `fromConsumptions should generate correct tooltips for DAY_HALF_HOURLY`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-07-04T10:00:00Z", end = "2024-07-04T10:30:00Z"),
            createConsumption(kWhConsumed = 10.0, start = "2024-07-04T10:30:00Z", end = "2024-07-04T11:00:00Z"),
            createConsumption(kWhConsumed = 15.0, start = "2024-07-04T11:00:00Z", end = "2024-07-04T11:30:00Z"),
        )
        val expectedTooltips = listOf(
            "11:00 - 11:30\n5.00 kWh",
            "11:30 - 12:00\n10.00 kWh",
            "12:00 - 12:30\n15.00 kWh",
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.DAY_HALF_HOURLY,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedTooltips, actual = barChartData.tooltips)
    }

    @Test
    fun `fromConsumptions should generate correct labels for WEEK_SEVEN_DAYS`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-07-01T00:00:00Z", end = "2024-07-01T23:59:59Z"), // Monday
            createConsumption(kWhConsumed = 10.0, start = "2024-07-02T00:00:00Z", end = "2024-07-02T23:59:59Z"), // Tuesday
            createConsumption(kWhConsumed = 15.0, start = "2024-07-03T00:00:00Z", end = "2024-07-03T23:59:59Z"), // Wednesday
        )
        val expectedLabels = mapOf(
            0 to "Mon",
            1 to "Tue",
            2 to "Wed",
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedLabels, actual = barChartData.labels)
    }

    @Test
    fun `fromConsumptions should generate correct tooltips for WEEK_SEVEN_DAYS`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-07-01T00:00:00Z", end = "2024-07-01T23:59:59Z"), // Monday
            createConsumption(kWhConsumed = 10.0, start = "2024-07-02T00:00:00Z", end = "2024-07-02T23:59:59Z"), // Tuesday
            createConsumption(kWhConsumed = 15.0, start = "2024-07-03T00:00:00Z", end = "2024-07-03T23:59:59Z"), // Wednesday
        )
        val expectedTooltips = listOf(
            "01 Jul\n5.00 kWh",
            "02 Jul\n10.00 kWh",
            "03 Jul\n15.00 kWh",
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.WEEK_SEVEN_DAYS,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedTooltips, actual = barChartData.tooltips)
    }

    @Test
    fun `fromConsumptions should generate correct labels for YEAR_TWELVE_MONTHS`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-01-01T00:00:00Z", end = "2024-01-31T23:59:59Z"), // January
            createConsumption(kWhConsumed = 10.0, start = "2024-02-01T00:00:00Z", end = "2024-02-28T23:59:59Z"), // February
            createConsumption(kWhConsumed = 15.0, start = "2024-03-01T00:00:00Z", end = "2024-03-31T23:59:59Z"), // March
        )
        val expectedLabels = mapOf(
            0 to "Jan",
            1 to "Feb",
            2 to "Mar",
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedLabels, actual = barChartData.labels)
    }

    @Test
    fun `fromConsumptions should generate correct tooltips for YEAR_TWELVE_MONTHS`() = runBlocking {
        val consumptions = listOf(
            createConsumption(kWhConsumed = 5.0, start = "2024-01-01T00:00:00Z", end = "2024-01-31T23:59:59Z"), // January
            createConsumption(kWhConsumed = 10.0, start = "2024-02-01T00:00:00Z", end = "2024-02-28T23:59:59Z"), // February
            createConsumption(kWhConsumed = 15.0, start = "2024-03-01T00:00:00Z", end = "2024-03-31T23:59:59Z"), // March
        )
        val expectedTooltips = listOf(
            "Jan 2024\n5.00 kWh",
            "Feb 2024\n10.00 kWh",
            "Mar 2024\n15.00 kWh",
        )

        val barChartData = BarChartData.fromConsumptions(
            presentationStyle = ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS,
            consumptions = consumptions,
            stringResourceProvider = MultiplatformStringResourceProvider(),
        )

        assertEquals(expected = expectedTooltips, actual = barChartData.tooltips)
    }
}
