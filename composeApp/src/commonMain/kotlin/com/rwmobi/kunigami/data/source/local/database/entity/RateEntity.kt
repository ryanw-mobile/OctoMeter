/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import kotlinx.datetime.Instant

@Entity(
    tableName = "rate",
    primaryKeys = ["tariff_code", "payment_method"],
)
data class RateEntity(
    @ColumnInfo(name = "tariff_code")
    val tariffCode: String,
    @ColumnInfo(name = "rate_type")
    val rateType: RateType,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: String,
    @ColumnInfo(name = "valid_from")
    val validFrom: Instant,
    @ColumnInfo(name = "valid_to")
    val validTo: Instant?,
    @ColumnInfo(name = "vat_Rate")
    val vatRate: Double,
)
