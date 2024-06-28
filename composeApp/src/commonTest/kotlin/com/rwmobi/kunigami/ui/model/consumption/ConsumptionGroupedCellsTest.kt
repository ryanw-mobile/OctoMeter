package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsumptionGroupedCellsTest {

    private val now = Clock.System.now()
    private val consumption1 = Consumption(
        kWhConsumed = 10.123,
        interval = now..now.plus(1, DateTimeUnit.HOUR),
    )
    private val consumption2 = Consumption(
        kWhConsumed = 20.456,
        interval = now..now.plus(2, DateTimeUnit.HOUR),
    )
    private val groupedCells = ConsumptionGroupedCells(
        title = "Test Group",
        consumptions = listOf(consumption1, consumption2),
    )

    @Test
    fun `getAggregateConsumption should return sum of consumptions`() {
        val aggregate = groupedCells.getAggregateConsumption()
        assertEquals(30.579, aggregate)
    }

    @Test
    fun `getAggregateConsumption should return rounded sum of consumptions`() {
        val groupedCellsList = listOf(groupedCells)
        val aggregateRounded = groupedCellsList.getAggregateConsumption(rounded = true)
        assertEquals(30.58, aggregateRounded)
    }

    @Test
    fun `getAggregateConsumption should return non-rounded sum of consumptions`() {
        val groupedCellsList = listOf(groupedCells)
        val aggregateNonRounded = groupedCellsList.getAggregateConsumption(rounded = false)
        assertEquals(30.579, aggregateNonRounded)
    }
}
