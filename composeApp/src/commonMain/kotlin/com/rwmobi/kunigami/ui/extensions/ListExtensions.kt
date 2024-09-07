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

package com.rwmobi.kunigami.ui.extensions

internal fun <T> List<T>.partitionList(columns: Int): List<List<T>> {
    if (columns <= 0) return listOf(this)

    val partitioned = MutableList(columns) { mutableListOf<T>() }
    val rows = (size + columns - 1) / columns // Calculate the number of rows needed

    for (index in indices) {
        val column = index / rows
        partitioned[column].add(this[index])
    }

    return partitioned.map { it.toList() }
}
