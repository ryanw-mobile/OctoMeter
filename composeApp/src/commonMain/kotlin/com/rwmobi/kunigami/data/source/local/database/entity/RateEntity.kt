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

package com.rwmobi.kunigami.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant

@Entity(
    tableName = "rate",
    primaryKeys = ["tariff_code", "rate_type", "payment_method", "valid_from"],
)
data class RateEntity(
    @ColumnInfo(name = "tariff_code")
    val tariffCode: String,
    @ColumnInfo(name = "rate_type")
    val rateType: RateType,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: PaymentMethod,
    @ColumnInfo(name = "valid_from")
    val validFrom: Instant,
    @ColumnInfo(name = "valid_to")
    val validTo: Instant?,
    @ColumnInfo(name = "vat_Rate")
    val vatRate: Double,
)

fun List<RateEntity>.coversRange(validFrom: Instant, validTo: Instant): Boolean {
    val sortedRates = this.sortedBy { it.validFrom }

    var currentEnd = validFrom

    for (rate in sortedRates) {
        if (rate.validFrom > currentEnd) {
            // There's a gap between currentEnd and the next rate's validFrom
            return false
        }

        currentEnd = rate.validTo ?: Instant.DISTANT_FUTURE

        if (currentEnd >= validTo) {
            // The range is covered up to or beyond the required validTo
            return true
        }
    }

    // After iterating all rates, we haven't covered the entire range
    return false
}
