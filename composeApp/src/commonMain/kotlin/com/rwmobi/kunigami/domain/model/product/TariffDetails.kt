/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

data class TariffDetails(
    val tariffPaymentTerm: TariffPaymentTerm,
    // The layered DTOs were flattened
    val tariffCode: String,
    val vatInclusiveStandingCharge: Double,
    val vatInclusiveOnlineDiscount: Double,
    val vatInclusiveDualFuelDiscount: Double,
    val exitFeesType: ExitFeesType,
    val vatInclusiveExitFees: Double,
    val vatInclusiveStandardUnitRate: Double?,
    val vatInclusiveDayUnitRate: Double?,
    val vatInclusiveNightUnitRate: Double?,
) {
    fun hasStandardUnitRate(): Boolean = vatInclusiveStandardUnitRate != null
    fun hasDualRates(): Boolean = (
        vatInclusiveDayUnitRate != null &&
            vatInclusiveNightUnitRate != null
        )
}
