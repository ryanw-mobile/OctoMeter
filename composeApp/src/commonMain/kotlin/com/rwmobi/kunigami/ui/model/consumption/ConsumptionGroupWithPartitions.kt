/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.model.consumption.Consumption

data class ConsumptionGroupWithPartitions(
    val title: String,
    val partitionedItems: List<List<Consumption>>,
)
