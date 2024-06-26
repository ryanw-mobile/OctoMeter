/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.datetime.Instant

@Entity(
    tableName = "standard_unit_rate",
    primaryKeys = ["tariff_code", "payment_method"],
)
data class StandardUnitRateEntity(
    @ColumnInfo(name = "tariff_code")
    val tariffCode: String,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: String?,
    @ColumnInfo(name = "valid_from")
    val validFrom: Instant,
    @ColumnInfo(name = "valid_to")
    val validTo: Instant?,
    @ColumnInfo(name = "vat_Rate")
    val vatRate: Double,
)
