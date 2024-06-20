package com.rwmobi.kunigami.domain.model.account

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration

class ElectricityMeterPointTest {
    private val now = Clock.System.now()
    private val agreement1 = Agreement(
        tariffCode = "TARIFF1",
        period = now.minus(Duration.parse("90d"))..now.minus(Duration.parse("60d")),
    )
    private val agreement2 = Agreement(
        tariffCode = "TARIFF2",
        period = now.minus(Duration.parse("60d"))..now.minus(Duration.parse("30d")),
    )
    private val agreement3 = Agreement(
        tariffCode = "TARIFF3",
        period = now.minus(Duration.parse("30d"))..now.plus(Duration.parse("30d")),
    )
    private val meterPoint = ElectricityMeterPoint(
        mpan = "MPAN1",
        meterSerialNumbers = listOf("METER1", "METER2"),
        agreements = listOf(agreement1, agreement2, agreement3),
    )

    @Test
    fun `lookupAgreement should return the agreement in effect at reference point`() {
        val agreement = meterPoint.lookupAgreement(now)
        assertEquals(agreement3, agreement)
    }

    @Test
    fun `lookupAgreement should return null if no agreement in effect at reference point`() {
        val referencePoint = now.minus(Duration.parse("100d"))
        val agreement = meterPoint.lookupAgreement(referencePoint)
        assertNull(agreement)
    }

    @Test
    fun `lookupAgreements should return agreements in effect within given date range`() {
        val validFrom = now.minus(Duration.parse("60d"))
        val validTo = now
        val agreements = meterPoint.lookupAgreements(period = validFrom..validTo)
        assertEquals(listOf(agreement2, agreement3), agreements)
    }

    @Test
    fun `lookupAgreements should return empty list if no agreements in effect within given date range`() {
        val validFrom = now.plus(Duration.parse("50d"))
        val validTo = now.plus(Duration.parse("50d"))
        val agreements = meterPoint.lookupAgreements(period = validFrom..validTo)
        assertTrue(agreements.isEmpty())
    }

    @Test
    fun `getLatestAgreement should return the agreement with the latest validTo date`() {
        val latestAgreement = meterPoint.getLatestAgreement()
        assertEquals(agreement3, latestAgreement)
    }

    @Test
    fun `getLatestAgreement should return null if there are no agreements`() {
        val emptyMeterPoint = ElectricityMeterPoint("MPAN1", listOf("METER1"), emptyList())
        val latestAgreement = emptyMeterPoint.getLatestAgreement()
        assertNull(latestAgreement)
    }
}
