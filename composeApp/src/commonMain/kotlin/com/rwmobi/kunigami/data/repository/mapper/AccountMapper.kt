/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.graphql.PropertiesQuery
import kotlinx.datetime.Instant

/***
 * Throws: IllegalArgumentException - if the timestamp return from API is not parsable
 */
fun PropertiesQuery.Property.toAccount(accountNumber: String): Account {
    // We decided to take the latest occupancy. This has no other side-effects other than a date showing on the screen.
    val occupancyPeriod = occupancyPeriods?.maxByOrNull {
        it?.effectiveFrom?.let { effectiveFrom ->
            Instant.parse(effectiveFrom.toString())
        } ?: Instant.DISTANT_PAST
    }

    return Account(
        accountNumber = accountNumber,
        movedInAt = occupancyPeriod?.effectiveFrom?.let { Instant.parse(it.toString()) },
        movedOutAt = occupancyPeriod?.effectiveTo?.let { Instant.parse(it.toString()) },
        fullAddress = address,
        postcode = postcode.uppercase(),
        electricityMeterPoints = electricityMeterPoints?.mapNotNull { it?.toElectricityMeterPoint() } ?: emptyList(),
    )
}

fun PropertiesQuery.ElectricityMeterPoint.toElectricityMeterPoint(): ElectricityMeterPoint {
    return ElectricityMeterPoint(
        mpan = mpan,
        meterSerialNumbers = meters?.mapNotNull { it?.serialNumber } ?: emptyList(),
        agreements = agreements?.filterNotNull()?.toAgreement()?.filterNotNull() ?: emptyList(),
    )
}

fun List<PropertiesQuery.Agreement>.toAgreement() = map {
    it.toAgreement()
}

fun PropertiesQuery.Agreement.toAgreement(): Agreement? {
    val period = Instant.parse(validFrom.toString())..(validTo?.let { Instant.parse(it.toString()) } ?: Instant.DISTANT_FUTURE)

    return when {
        tariff?.onStandardTariff != null -> tariff.onStandardTariff.toAgreement(period = period)
        tariff?.onHalfHourlyTariff != null -> tariff.onHalfHourlyTariff.toAgreement(period = period)
        tariff?.onDayNightTariff != null -> tariff.onDayNightTariff.toAgreement(period = period)
        tariff?.onThreeRateTariff != null -> tariff.onThreeRateTariff.toAgreement(period = period)
        tariff?.onPrepayTariff != null -> tariff.onPrepayTariff.toAgreement(period = period)

        else -> {
            null
        }
    }
}

private fun PropertiesQuery.OnStandardTariff.toAgreement(period: ClosedRange<Instant>): Agreement? {
    return if (tariffCode != null &&
        fullName != null &&
        displayName != null &&
        description != null &&
        standingCharge != null
    ) {
        Agreement(
            tariffCode = tariffCode,
            period = period,
            fullName = fullName,
            displayName = displayName,
            description = description,
            isHalfHourlyTariff = false,
            vatInclusiveStandingCharge = standingCharge,
            vatInclusiveStandardUnitRate = unitRate,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = null,
            vatInclusiveOffPeakRate = null,
            agilePriceCap = null,
        )
    } else {
        null
    }
}

private fun PropertiesQuery.OnHalfHourlyTariff.toAgreement(period: ClosedRange<Instant>): Agreement? {
    return if (tariffCode != null &&
        fullName != null &&
        displayName != null &&
        description != null &&
        standingCharge != null
    ) {
        Agreement(
            tariffCode = tariffCode,
            period = period,
            fullName = fullName,
            displayName = displayName,
            description = description,
            isHalfHourlyTariff = true,
            vatInclusiveStandingCharge = standingCharge,
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = null,
            vatInclusiveOffPeakRate = null,
            agilePriceCap = agileCalculationInfo?.priceCap,
        )
    } else {
        null
    }
}

private fun PropertiesQuery.OnDayNightTariff.toAgreement(period: ClosedRange<Instant>): Agreement? {
    return if (tariffCode != null &&
        fullName != null &&
        displayName != null &&
        description != null &&
        standingCharge != null
    ) {
        Agreement(
            tariffCode = tariffCode,
            period = period,
            fullName = fullName,
            displayName = displayName,
            description = description,
            isHalfHourlyTariff = false,
            vatInclusiveStandingCharge = standingCharge,
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = dayRate,
            vatInclusiveNightUnitRate = nightRate,
            vatInclusiveOffPeakRate = null,
            agilePriceCap = null,
        )
    } else {
        null
    }
}

private fun PropertiesQuery.OnThreeRateTariff.toAgreement(period: ClosedRange<Instant>): Agreement? {
    return if (tariffCode != null &&
        fullName != null &&
        displayName != null &&
        description != null &&
        standingCharge != null
    ) {
        Agreement(
            tariffCode = tariffCode,
            period = period,
            fullName = fullName,
            displayName = displayName,
            description = description,
            isHalfHourlyTariff = false,
            vatInclusiveStandingCharge = standingCharge,
            vatInclusiveStandardUnitRate = null,
            vatInclusiveDayUnitRate = dayRate,
            vatInclusiveNightUnitRate = nightRate,
            vatInclusiveOffPeakRate = offPeakRate,
            agilePriceCap = null,
        )
    } else {
        null
    }
}

private fun PropertiesQuery.OnPrepayTariff.toAgreement(period: ClosedRange<Instant>): Agreement? {
    return if (tariffCode != null &&
        fullName != null &&
        displayName != null &&
        description != null &&
        standingCharge != null
    ) {
        Agreement(
            tariffCode = tariffCode,
            period = period,
            fullName = fullName,
            displayName = displayName,
            description = description,
            isHalfHourlyTariff = false,
            vatInclusiveStandingCharge = standingCharge,
            vatInclusiveStandardUnitRate = unitRate,
            vatInclusiveDayUnitRate = null,
            vatInclusiveNightUnitRate = null,
            vatInclusiveOffPeakRate = null,
            agilePriceCap = null,
        )
    } else {
        null
    }
}
