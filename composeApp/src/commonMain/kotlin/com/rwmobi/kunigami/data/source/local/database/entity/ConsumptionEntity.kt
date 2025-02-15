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
import kotlinx.datetime.Instant

@Entity(
    tableName = "consumption",
    primaryKeys = ["device_id", "interval_start"],
)
data class ConsumptionEntity(
    @ColumnInfo(name = "device_id")
    val deviceId: String,
    @ColumnInfo(name = "interval_start")
    val intervalStart: Instant,
    @ColumnInfo(name = "interval_end")
    val intervalEnd: Instant,
    @ColumnInfo(name = "kwh_consumed")
    val kWhConsumed: Double,
    @ColumnInfo(name = "consumption_cost")
    val consumptionCost: Double,
)
