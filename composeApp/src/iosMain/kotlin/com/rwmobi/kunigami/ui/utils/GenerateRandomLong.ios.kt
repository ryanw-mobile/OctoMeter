/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import platform.Foundation.NSUUID

@OptIn(ExperimentalForeignApi::class)
actual fun generateRandomLong(): Long {
    // Proposed by ChatGPT
    val uuid = NSUUID()
    return memScoped {
        val bytes = allocArray<UByteVar>(16) // Allocate memory for 16 unsigned bytes of UUID
        uuid.getUUIDBytes(bytes) // Fill the array with the UUID bytes

        // Constructing the Long from the first 8 bytes
        var mostSignificantBits: Long = 0
        for (i in 0 until 8) {
            mostSignificantBits = (mostSignificantBits shl 8) or (bytes[i].toInt() and 0xFF).toLong()
        }
        mostSignificantBits
    }
}
