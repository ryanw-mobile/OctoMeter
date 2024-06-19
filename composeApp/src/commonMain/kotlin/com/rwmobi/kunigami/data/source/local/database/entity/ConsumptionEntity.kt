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
    tableName = "consumption",
    primaryKeys = ["meter_serial", "interval_start"],
)
data class ConsumptionEntity(
    @ColumnInfo(name = "meter_serial")
    val meterSerial: String,
    @ColumnInfo(name = "interval_start")
    val intervalStart: Instant,
    @ColumnInfo(name = "interval_end")
    val intervalEnd: Instant,
    @ColumnInfo(name = "kwh_consumed")
    val kWhConsumed: Double,
)
