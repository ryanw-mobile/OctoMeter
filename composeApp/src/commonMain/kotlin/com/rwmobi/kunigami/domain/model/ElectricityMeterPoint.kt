/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

data class ElectricityMeterPoint(
    val mpan: String,
    val meterSerialNumbers: List<String>,
    val agreements: List<Agreement>,
)