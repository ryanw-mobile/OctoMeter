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

package com.rwmobi.kunigami.domain.usecase.consumption

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.test.samples.TariffSampleData
import com.rwmobi.kunigami.ui.model.consumption.Insights
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GenerateUsageInsightsUseCaseTest {

    private val tariff = TariffSampleData.var221101
    private val consumption1 = Consumption(
        kWhConsumed = 10.0,
        interval = Instant.parse("2023-03-30T01:00:00Z")..Instant.parse("2023-03-30T01:30:00Z"),
    )
    private val consumption2 = Consumption(
        kWhConsumed = 15.0,
        interval = Instant.parse("2023-03-30T01:30:00Z")..Instant.parse("2023-03-30T02:00:00Z"),
    )
    private val consumption3 = Consumption(
        kWhConsumed = 15.0,
        interval = Instant.parse("2023-03-30T02:00:00Z")..Instant.parse("2023-03-30T02:30:00Z"),
    )
    private val consumptionWithCost1 = ConsumptionWithCost(consumption = consumption1, vatInclusiveCost = 45.0, vatInclusiveStandingCharge = 65.0)
    private val consumptionWithCost2 = ConsumptionWithCost(consumption = consumption2, vatInclusiveCost = 5.0, vatInclusiveStandingCharge = 65.0)
    private val consumptionWithCost3 = ConsumptionWithCost(consumption = consumption3, vatInclusiveCost = 16.0, vatInclusiveStandingCharge = 65.0)
    private val generateUsageInsightsUseCase = GenerateUsageInsightsUseCase()

    @Test
    fun `generateUsageInsightsUseCase should return null when tariff is null`() {
        val result = generateUsageInsightsUseCase(
            tariff = null,
            consumptionWithCost = listOf(consumptionWithCost1),
        )
        assertNull(result)
    }

    @Test
    fun `generateUsageInsightsUseCase should return null when consumptionWithCost is null`() {
        val result = generateUsageInsightsUseCase(
            tariff = tariff,
            consumptionWithCost = null,
        )
        assertNull(result)
    }

    @Test
    fun `generateUsageInsightsUseCase should return null when consumptionWithCost is empty`() {
        val result = generateUsageInsightsUseCase(
            tariff = tariff,
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
            costDailyAverage = 1.14,
            consumptionAnnualProjection = 14600.0,
            costAnnualProjection = 415.55,
        )
        val result = generateUsageInsightsUseCase(
            tariff = tariff,
            consumptionWithCost = listOf(consumptionWithCost1, consumptionWithCost2, consumptionWithCost3),
        )
        assertEquals(expectedInsights, result)
    }

    @Test
    fun `generateUsageInsightsUseCase should return correct Insights when isTrueCost is false`() {
        val consumptionWithCostWithoutCost = ConsumptionWithCost(consumption = consumption1, vatInclusiveCost = null, vatInclusiveStandingCharge = null)
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
            tariff = tariff,
            consumptionWithCost = listOf(consumptionWithCostWithoutCost),
        )
        assertEquals(expectedInsights, result)
    }
}
