/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
