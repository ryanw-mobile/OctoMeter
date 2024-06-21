/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import io.ktor.util.network.UnresolvedAddressException
import java.net.UnknownHostException

actual fun Throwable.mapFromPlatform(): Throwable {
    return when (this) {
        is UnknownHostException -> {
            UnresolvedAddressException()
        }

        else -> this
    }
}
