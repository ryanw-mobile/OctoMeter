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

package com.rwmobi.kunigami.domain.model.account

import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class AccountTest {

    private val account = AccountSamples.account928
    private val firstMpan = AccountSamples.account928.electricityMeterPoints.first()

    @Test
    fun `getDefaultLatestTariffCode should return latest tariff code`() {
        val defaultTariffCode = account.getDefaultLatestTariffCode()
        assertEquals(AccountSamples.agreementE1RVAR231101C.tariffCode, defaultTariffCode)
    }

    @Test
    fun `getDefaultLatestTariffCode should return tariff code for given MPAN`() {
        val tariffCode = account.getDefaultLatestTariffCode(firstMpan.mpan)
        assertEquals(AccountSamples.agreementE1RVAR231101C.tariffCode, tariffCode)
    }

    @Test
    fun `getDefaultLatestTariffCode should return null for unknown MPAN`() {
        val tariffCode = account.getDefaultLatestTariffCode("UNKNOWN_MPAN")
        assertNull(tariffCode)
    }

    @Test
    fun `getDefaultMpan should return first MPAN`() {
        val defaultMpan = account.getDefaultMpan()
        assertEquals(firstMpan.mpan, defaultMpan)
    }

    @Test
    fun `containsMpan should return true for existing MPAN`() {
        val containsMpan = account.containsMpan(firstMpan.mpan)
        assertTrue(containsMpan)
    }

    @Test
    fun `containsMpan should return false for unknown MPAN`() {
        val containsMpan = account.containsMpan("UNKNOWN_MPAN")
        assertFalse(containsMpan)
    }

    @Test
    fun `getDefaultMeterSerialNumber should return first meter serial number`() {
        val defaultMeterSerialNumber = account.getDefaultMeterSerialNumber()
        assertEquals(firstMpan.meters.first().serialNumber, defaultMeterSerialNumber)
    }

    @Test
    fun `containsMeterSerialNumber should return true for existing MPAN and serial number`() {
        val containsMeterSerialNumber = account.containsMeterSerialNumber(
            mpan = firstMpan.mpan,
            meterSerialNumber = firstMpan.meters.first().serialNumber,
        )
        assertTrue(containsMeterSerialNumber)
    }

    @Test
    fun `containsMeterSerialNumber should return false for unknown MPAN`() {
        val containsMeterSerialNumber = account.containsMeterSerialNumber(
            mpan = "UNKNOWN_MPAN",
            meterSerialNumber = firstMpan.meters.first().serialNumber,
        )
        assertFalse(containsMeterSerialNumber)
    }

    @Test
    fun `containsMeterSerialNumber should return false for unknown serial number`() {
        val containsMeterSerialNumber = account.containsMeterSerialNumber(
            mpan = firstMpan.mpan,
            meterSerialNumber = "UNKNOWN_METER",
        )
        assertFalse(containsMeterSerialNumber)
    }
}
