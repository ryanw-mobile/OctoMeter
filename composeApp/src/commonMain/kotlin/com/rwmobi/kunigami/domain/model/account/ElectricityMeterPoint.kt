/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Immutable
data class ElectricityMeterPoint(
    val mpan: String,
    val meters: List<ElectricityMeter>,
    val agreements: List<Agreement>,
) {
    /**
     * Returns the specific agreement in effect on a particular day.
     * Note: previous agreement end day = new agreement start day
     * Returns null if not found
     */
    fun lookupAgreement(referencePoint: Instant = Clock.System.now()): Agreement? {
        return agreements.firstOrNull { agreement ->
            val isAvailableFrom = agreement.period.start <= referencePoint
            val isAvailableTo = agreement.period.endInclusive > referencePoint
            isAvailableFrom && isAvailableTo
        }
    }

    /**
     * Return all agreements in effect for a given date range.
     * Note: previous agreement end day = new agreement start day
     * Returns empty list if no matching result
     */
    fun lookupAgreements(period: ClosedRange<Instant>): List<Agreement> {
        return agreements.filter { agreement ->
            agreement.period.start <= period.endInclusive && agreement.period.endInclusive > period.start
        }
    }

    /**
     * Get the agreement with the latest validTo date.
     * Returns null if the list is empty
     */
    fun getLatestAgreement(): Agreement? {
        return agreements.maxByOrNull { it.period.endInclusive }
    }

    /**
     * Returns the start date of the first tariff for this MPAN
     */
    fun getFirstTariffStartDate(): Instant? {
        return agreements.minByOrNull { it.period.start }?.period?.start
    }
}
