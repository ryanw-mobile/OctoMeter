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

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration

@Suppress("TooManyFunctions")
class ElectricityMeterPointTest {
    private val now = Clock.System.now()
    private val agreement1 = Agreement(
        tariffCode = "TARIFF1",
        period = now.minus(Duration.parse("90d"))..now.minus(Duration.parse("60d")),
        fullName = "Sample tariff name",
        displayName = "Sample display name",
        description = "Sample description",
        isHalfHourlyTariff = false,
        vatInclusiveStandingCharge = 39.768,
        vatInclusiveStandardUnitRate = 13.95,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
        agilePriceCap = null,
    )
    private val agreement2 = Agreement(
        tariffCode = "TARIFF2",
        period = now.minus(Duration.parse("60d"))..now.minus(Duration.parse("30d")),
        fullName = "Sample tariff name",
        displayName = "Sample display name",
        description = "Sample description",
        isHalfHourlyTariff = false,
        vatInclusiveStandingCharge = 39.768,
        vatInclusiveStandardUnitRate = 13.95,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
        agilePriceCap = null,
    )
    private val agreement3 = Agreement(
        tariffCode = "TARIFF3",
        period = now.minus(Duration.parse("30d"))..now.plus(Duration.parse("30d")),
        fullName = "Sample tariff name",
        displayName = "Sample display name",
        description = "Sample description",
        isHalfHourlyTariff = false,
        vatInclusiveStandingCharge = 39.768,
        vatInclusiveStandardUnitRate = 13.95,
        vatInclusiveDayUnitRate = null,
        vatInclusiveNightUnitRate = null,
        vatInclusiveOffPeakRate = null,
        agilePriceCap = null,
    )
    private val meterPointWithTwoMeters = ElectricityMeterPoint(
        mpan = "MPAN1",
        meters = listOf(
            ElectricityMeter(
                serialNumber = "METER1",
                makeAndType = null,
                readingSource = null,
                readAt = null,
                value = null,
            ),
            ElectricityMeter(
                serialNumber = "METER2",
                makeAndType = null,
                readingSource = null,
                readAt = null,
                value = null,
            ),
        ),
        agreements = listOf(agreement1, agreement2, agreement3),
    )
    private val emptyMeterPoint = ElectricityMeterPoint(
        mpan = "MPAN1",
        meters = listOf(
            ElectricityMeter(
                serialNumber = "METER1",
                makeAndType = null,
                readingSource = null,
                readAt = null,
                value = null,
            ),
        ),
        agreements = emptyList(),
    )

    @Test
    fun `lookupAgreement should return the agreement in effect at reference point`() {
        val agreement = meterPointWithTwoMeters.lookupAgreement(now)
        assertEquals(agreement3, agreement)
    }

    @Test
    fun `lookupAgreement should return null if no agreement in effect at reference point`() {
        val referencePoint = now.minus(Duration.parse("100d"))
        val agreement = meterPointWithTwoMeters.lookupAgreement(referencePoint)
        assertNull(agreement)
    }

    @Test
    fun `lookupAgreements should return agreements in effect within given date range`() {
        val validFrom = now.minus(Duration.parse("60d"))
        val validTo = now
        val agreements = meterPointWithTwoMeters.lookupAgreements(period = validFrom..validTo)
        assertEquals(listOf(agreement2, agreement3), agreements)
    }

    @Test
    fun `lookupAgreements should return empty list if no agreements in effect within given date range`() {
        val validFrom = now.plus(Duration.parse("50d"))
        val validTo = now.plus(Duration.parse("50d"))
        val agreements = meterPointWithTwoMeters.lookupAgreements(period = validFrom..validTo)
        assertTrue(agreements.isEmpty())
    }

    @Test
    fun `getLatestAgreement should return the agreement with the latest validTo date`() {
        val latestAgreement = meterPointWithTwoMeters.getLatestAgreement()
        assertEquals(agreement3, latestAgreement)
    }

    @Test
    fun `getLatestAgreement should return null if there are no agreements`() {
        val meterPoint = emptyMeterPoint
        val latestAgreement = meterPoint.getLatestAgreement()
        assertNull(latestAgreement)
    }

    @Test
    fun `getFirstTariffStartDate should return the date with the first validFrom date`() {
        val firstTariffStartDate = meterPointWithTwoMeters.getFirstTariffStartDate()
        assertEquals(agreement1.period.start, firstTariffStartDate)
    }

    @Test
    fun `getFirstTariffStartDate should return null if there are no agreements`() {
        val meterPoint = emptyMeterPoint
        val firstTariffStartDate = meterPoint.getFirstTariffStartDate()
        assertNull(firstTariffStartDate)
    }
}
