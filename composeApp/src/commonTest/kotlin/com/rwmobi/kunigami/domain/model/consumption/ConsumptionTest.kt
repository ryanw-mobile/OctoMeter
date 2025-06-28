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

package com.rwmobi.kunigami.domain.model.consumption

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
class ConsumptionTest {

    private val londonZone = TimeZone.of("Europe/London")

    @Test
    fun `getConsumptionDaySpan should return correct time span in days`() {
        val consumptionList = listOf(
            Consumption(
                kWhConsumed = 10.0,
                interval = LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 1,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone)..LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 1,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
            Consumption(
                kWhConsumed = 15.0,
                interval = LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 2,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone)..LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 2,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
        )
        val expectedTimeSpan = 2 // Since it's spanning 2 different days

        val actualTimeSpan = consumptionList.getConsumptionDaySpan()

        assertEquals(expectedTimeSpan, actualTimeSpan)
    }

    @Test
    fun `getConsumptionDaySpan should return 0 for empty list`() {
        val consumptionList = emptyList<Consumption>()
        val expectedTimeSpan = 0

        val actualTimeSpan = consumptionList.getConsumptionDaySpan()

        assertEquals(expectedTimeSpan, actualTimeSpan)
    }

    @Test
    fun `getConsumptionRange should return correct range for non-empty list`() {
        val consumptionList = listOf(
            Consumption(
                kWhConsumed = 10.0,
                interval = LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 1,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone)..LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 1,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
            Consumption(
                kWhConsumed = 15.0,
                interval = LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 2,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone)..LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 2,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
            Consumption(
                kWhConsumed = 12.5,
                interval = LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 3,
                    hour = 0,
                    minute = 0,
                ).toInstant(londonZone)..LocalDateTime(
                    year = 2023,
                    month = 5,
                    day = 3,
                    hour = 1,
                    minute = 0,
                ).toInstant(londonZone),
            ),
        )
        val expectedRange = 0.0..15.0 // ceil(15.0 * 10) / 10.0 = 15.0

        val actualRange = consumptionList.getConsumptionRange()

        assertEquals(expectedRange, actualRange)
    }

    @Test
    fun `getConsumptionRange should return 0_0 to 0_0 for empty list`() {
        val consumptionList = emptyList<Consumption>()
        val expectedRange = 0.0..0.0

        val actualRange = consumptionList.getConsumptionRange()

        assertEquals(expectedRange, actualRange)
    }
}
