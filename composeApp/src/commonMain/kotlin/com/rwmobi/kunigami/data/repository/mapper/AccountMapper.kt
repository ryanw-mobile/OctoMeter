/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.account.AgreementDto
import com.rwmobi.kunigami.data.source.network.dto.account.ElectricityMeterPointDto
import com.rwmobi.kunigami.data.source.network.dto.account.PropertyDto
import com.rwmobi.kunigami.data.source.network.extensions.capitalizeWords
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import kotlinx.datetime.Instant

fun PropertyDto.toAccount(accountNumber: String) = Account(
    id = id,
    accountNumber = accountNumber,
    movedInAt = movedInAt,
    movedOutAt = movedOutAt,
    fullAddress = mergeAddress(
        addressLine1.capitalizeWords(),
        addressLine2.capitalizeWords(),
        addressLine3.capitalizeWords(),
        town.capitalizeWords(),
        county.capitalizeWords(),
        postcode.uppercase(),
    ),
    postcode = postcode.uppercase(),
    electricityMeterPoints = electricityMeterPoints.map { it.toElectricityMeterPoint() },
)

fun ElectricityMeterPointDto.toElectricityMeterPoint(): ElectricityMeterPoint {
    return ElectricityMeterPoint(
        mpan = mpan,
        meterSerialNumbers = meters.map { it.serialNumber },
        agreements = agreements.toAgreement(),
    )
}

fun AgreementDto.toAgreement() = Agreement(
    tariffCode = tariffCode,
    period = validFrom..(validTo ?: Instant.DISTANT_FUTURE),
)

fun List<AgreementDto>.toAgreement() = map {
    it.toAgreement()
}

private fun mergeAddress(vararg addressLines: String?): String? {
    val filteredAddressLines = addressLines.toList().filterNotNull().filter { it.trim().isNotBlank() }
    if (filteredAddressLines.isEmpty()) return null
    return filteredAddressLines.joinToString(separator = "\n")
}
