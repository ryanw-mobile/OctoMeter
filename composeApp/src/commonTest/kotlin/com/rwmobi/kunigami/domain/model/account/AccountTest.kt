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
        assertEquals(firstMpan.meterSerialNumbers.first(), defaultMeterSerialNumber)
    }

    @Test
    fun `containsMeterSerialNumber should return true for existing MPAN and serial number`() {
        val containsMeterSerialNumber = account.containsMeterSerialNumber(
            mpan = firstMpan.mpan,
            serial = firstMpan.meterSerialNumbers.first(),
        )
        assertTrue(containsMeterSerialNumber)
    }

    @Test
    fun `containsMeterSerialNumber should return false for unknown MPAN`() {
        val containsMeterSerialNumber = account.containsMeterSerialNumber(
            mpan = "UNKNOWN_MPAN",
            serial = firstMpan.meterSerialNumbers.first(),
        )
        assertFalse(containsMeterSerialNumber)
    }

    @Test
    fun `containsMeterSerialNumber should return false for unknown serial number`() {
        val containsMeterSerialNumber = account.containsMeterSerialNumber(
            mpan = firstMpan.mpan,
            serial = "UNKNOWN_METER",
        )
        assertFalse(containsMeterSerialNumber)
    }
}
