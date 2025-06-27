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

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

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
