/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import com.rwmobi.kunigami.domain.exceptions.HttpException
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.util.network.UnresolvedAddressException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorNotConnectedToInternet

/***
 * This is needed to handle Ktor Darwin Engine exceptions which does not exist in CommonMain
 */
actual fun Throwable.mapFromPlatform(): Throwable {
    return when (this) {
        is DarwinHttpRequestException -> {
            // Check if the underlying error indicates no network
            val nsError = (origin as? NSError)
            if (nsError?.code == NSURLErrorNotConnectedToInternet) {
                // No network connection available
                UnresolvedAddressException()
            } else {
                this
            }
        }

        is ClientRequestException -> {
            HttpException(httpStatusCode = response.status.value)
        }

        else -> this
    }
}
