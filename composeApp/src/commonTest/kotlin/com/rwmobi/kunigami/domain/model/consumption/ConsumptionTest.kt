/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.consumption

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsumptionTest {

    private val londonZone = TimeZone.of("Europe/London")

    @Test
    fun `getConsumptionTimeSpan should return correct time span in days`() {
        val consumptionList = listOf(
            Consumption(
                consumption = 10.0,
                intervalStart = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 1,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone),
                intervalEnd = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 1,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
            Consumption(
                consumption = 15.0,
                intervalStart = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 2,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone),
                intervalEnd = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 2,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
        )
        val expectedTimeSpan = 2 // Since it's spanning 2 different days

        val actualTimeSpan = consumptionList.getConsumptionTimeSpan()

        assertEquals(expectedTimeSpan, actualTimeSpan)
    }

    @Test
    fun `getConsumptionTimeSpan should return 0 for empty list`() {
        val consumptionList = emptyList<Consumption>()
        val expectedTimeSpan = 0

        val actualTimeSpan = consumptionList.getConsumptionTimeSpan()

        assertEquals(expectedTimeSpan, actualTimeSpan)
    }

    @Test
    fun `getRange should return correct range for non-empty list`() {
        val consumptionList = listOf(
            Consumption(
                consumption = 10.0,
                intervalStart = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 1,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone),
                intervalEnd = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 1,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
            Consumption(
                consumption = 15.0,
                intervalStart = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 2,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone),
                intervalEnd = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 2,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
            Consumption(
                consumption = 12.5,
                intervalStart = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 3,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone),
                intervalEnd = LocalDateTime(
                    year = 2023,
                    monthNumber = 5,
                    dayOfMonth = 3,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
        )
        val expectedRange = 0.0..15.0 // ceil(15.0 * 10) / 10.0 = 15.0

        val actualRange = consumptionList.getRange()

        assertEquals(expectedRange, actualRange)
    }

    @Test
    fun `getRange should return 0_0 to 0_0 for empty list`() {
        val consumptionList = emptyList<Consumption>()
        val expectedRange = 0.0..0.0

        val actualRange = consumptionList.getRange()

        assertEquals(expectedRange, actualRange)
    }
}
