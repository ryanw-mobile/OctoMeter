/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database

import androidx.room.TypeConverter
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import kotlinx.datetime.Instant

class InstantConverters {
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

    @TypeConverter
    fun fromRateType(rateType: RateType): Int {
        return rateType.id
    }

    @TypeConverter
    fun toRateType(id: Int): RateType {
        return RateType.entries.first { it.id == id }
    }
}
