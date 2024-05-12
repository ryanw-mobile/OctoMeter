/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.network.dto.AgreementDto
import com.rwmobi.kunigami.data.source.network.dto.ElectricityMeterPointDto
import com.rwmobi.kunigami.data.source.network.dto.PropertyDto
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Agreement
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint

fun PropertyDto.toAccount(accountNumber: String) = Account(
    id = id,
    accountNumber = accountNumber,
    movedInAt = movedInAt,
    movedOutAt = movedOutAt,
    fullAddress = mergeAddress(
        addressLine1,
        addressLine2,
        addressLine3,
        town,
        county,
        postcode,
    ),
    electricityMeterPoints = electricityMeterPoints.map { it.toElectricityMeterPoint() },
)

fun ElectricityMeterPointDto.toElectricityMeterPoint() = ElectricityMeterPoint(
    mpan = mpan,
    meterSerialNumbers = meters.map { it.serialNumber },
    agreements = agreements.map { it.toAgreement() },
)

fun AgreementDto.toAgreement() = Agreement(
    tariffCode = tariffCode,
    validFrom = validFrom,
    validTo = validTo,
)

private fun mergeAddress(vararg addressLines: String?): String? {
    val filteredAddressLines = addressLines.toList().filterNotNull().filter { it.trim().isNotBlank() }
    if (filteredAddressLines.isEmpty()) return null
    return filteredAddressLines.joinToString(separator = "\n")
}
