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
