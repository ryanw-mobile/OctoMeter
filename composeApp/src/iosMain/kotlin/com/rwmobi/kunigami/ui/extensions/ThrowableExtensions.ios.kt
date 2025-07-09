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

import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.rwmobi.kunigami.domain.exceptions.HttpException
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.util.network.UnresolvedAddressException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorNotConnectedToInternet

/***
 * This is needed to handle Ktor Darwin Engine exceptions which does not exist in CommonMain
 */
actual fun Throwable.mapFromPlatform(): Throwable = when (this) {
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

    is ApolloHttpException -> {
        HttpException(httpStatusCode = statusCode)
    }

    is ApolloNetworkException -> {
        UnresolvedAddressException()
    }

    else -> this
}
