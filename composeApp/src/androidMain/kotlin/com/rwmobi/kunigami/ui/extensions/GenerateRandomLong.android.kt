/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import java.util.UUID

actual fun generateRandomLong(): Long {
    return UUID.randomUUID().mostSignificantBits
}
