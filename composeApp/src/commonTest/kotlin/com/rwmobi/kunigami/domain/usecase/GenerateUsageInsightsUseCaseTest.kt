/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.samples.TariffSampleData
import com.rwmobi.kunigami.ui.model.consumption.Insights
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GenerateUsageInsightsUseCaseTest {

    private val tariffSummary = TariffSampleData.var221101
    private val consumption1 = Consumption(
        kWhConsumed = 10.0,
        intervalStart = Instant.parse("2023-03-30T01:00:00Z"),
        intervalEnd = Instant.parse("2023-03-30T01:30:00Z"),
    )
    private val consumption2 = Consumption(
        kWhConsumed = 15.0,
        intervalStart = Instant.parse("2023-03-30T01:30:00Z"),
        intervalEnd = Instant.parse("2023-03-30T02:00:00Z"),
    )
    private val consumption3 = Consumption(
        kWhConsumed = 15.0,
        intervalStart = Instant.parse("2023-03-30T02:00:00Z"),
        intervalEnd = Instant.parse("2023-03-30T02:30:00Z"),
    )
    private val consumptionWithCost1 = ConsumptionWithCost(consumption = consumption1, vatInclusiveCost = 45.0)
    private val consumptionWithCost2 = ConsumptionWithCost(consumption = consumption2, vatInclusiveCost = 5.0)
    private val consumptionWithCost3 = ConsumptionWithCost(consumption = consumption3, vatInclusiveCost = 16.0)
    private val generateUsageInsightsUseCase = GenerateUsageInsightsUseCase()

    @Test
    fun `generateUsageInsightsUseCase should return null when tariffSummary is null`() {
        val result = generateUsageInsightsUseCase(
            tariffSummary = null,
            consumptionWithCost = listOf(consumptionWithCost1),
        )
        assertNull(result)
    }

    @Test
    fun `generateUsageInsightsUseCase should return null when consumptionWithCost is null`() {
        val result = generateUsageInsightsUseCase(
            tariffSummary = tariffSummary,
            consumptionWithCost = null,
        )
        assertNull(result)
    }

    @Test
    fun `generateUsageInsightsUseCase should return null when consumptionWithCost is empty`() {
        val result = generateUsageInsightsUseCase(
            tariffSummary = tariffSummary,
            consumptionWithCost = emptyList(),
        )
        assertNull(result)
    }

    @Test
    fun `generateUsageInsightsUseCase should return correct Insights when inputs are valid`() {
        val expectedInsights = Insights(
            consumptionAggregateRounded = 40.0,
            consumptionTimeSpan = 1,
            consumptionChargeRatio = 0.58,
            costWithCharges = 1.14,
            isTrueCost = true,
            consumptionDailyAverage = 40.0,
            costDailyAverage = 10.58,
            consumptionAnnualProjection = 14600.0,
            costAnnualProjection = 415.55,
        )
        val result = generateUsageInsightsUseCase(
            tariffSummary = tariffSummary,
            consumptionWithCost = listOf(consumptionWithCost1, consumptionWithCost2, consumptionWithCost3),
        )
        assertEquals(result, expectedInsights)
    }

    @Test
    fun `generateUsageInsightsUseCase should return correct Insights when isTrueCost is false`() {
        val consumptionWithCostWithoutCost = ConsumptionWithCost(consumption = consumption1, vatInclusiveCost = null)
        val expectedInsights = Insights(
            consumptionAggregateRounded = 10.0,
            consumptionTimeSpan = 1,
            consumptionChargeRatio = 0.84,
            costWithCharges = 3.0,
            isTrueCost = false,
            consumptionDailyAverage = 10.0,
            costDailyAverage = 3.0,
            consumptionAnnualProjection = 3650.0,
            costAnnualProjection = 1096.28,
        )
        val result = generateUsageInsightsUseCase(
            tariffSummary = tariffSummary,
            consumptionWithCost = listOf(consumptionWithCostWithoutCost),
        )
        assertEquals(result, expectedInsights)
    }
}
