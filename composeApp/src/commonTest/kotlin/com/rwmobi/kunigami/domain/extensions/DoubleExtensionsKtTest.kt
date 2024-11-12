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

import com.rwmobi.kunigami.domain.extensions.roundConsumptionToNearestEvenHundredth
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
class DoubleExtensionsKtTest {

    @Test
    fun `roundToNearestEvenHundredth should round 0_015 to 0_02`() {
        val input = 0.015
        val expected = 0.02
        val result = input.roundConsumptionToNearestEvenHundredth()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToNearestEvenHundredth should round 0_025 to 0_02`() {
        val input = 0.025
        val expected = 0.02
        val result = input.roundConsumptionToNearestEvenHundredth()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToNearestEvenHundredth should round 0_045 to 0_04`() {
        val input = 0.045
        val expected = 0.04
        val result = input.roundConsumptionToNearestEvenHundredth()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToNearestEvenHundredth should round 0_055 to 0_06`() {
        val input = 0.055
        val expected = 0.06
        val result = input.roundConsumptionToNearestEvenHundredth()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToTwoDecimalPlaces should round 0_1234 to 0_12`() {
        val input = 0.1234
        val expected = 0.12
        val result = input.roundToTwoDecimalPlaces()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToTwoDecimalPlaces should round 0_1265 to 0_13`() {
        val input = 0.1265
        val expected = 0.13
        val result = input.roundToTwoDecimalPlaces()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToTwoDecimalPlaces should round 0_1245 to 0_12`() {
        val input = 0.1245
        val expected = 0.12
        val result = input.roundToTwoDecimalPlaces()
        assertEquals(expected, result)
    }

    @Test
    fun `roundToTwoDecimalPlaces should round 0_1255 to 0_13`() {
        val input = 0.1255
        val expected = 0.13
        val result = input.roundToTwoDecimalPlaces()
        assertEquals(expected, result)
    }
}
