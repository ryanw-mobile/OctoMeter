/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model.rate

import com.rwmobi.kunigami.domain.model.rate.Rate

data class RateGroupWithPartitions(
    val title: String,
    val partitionedItems: List<List<Rate>>,
)
