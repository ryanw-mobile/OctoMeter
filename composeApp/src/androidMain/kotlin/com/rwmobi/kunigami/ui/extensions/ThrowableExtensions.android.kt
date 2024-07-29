/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.rwmobi.kunigami.domain.exceptions.HttpException
import io.ktor.util.network.UnresolvedAddressException
import java.net.UnknownHostException

actual fun Throwable.mapFromPlatform(): Throwable {
    return when (this) {
        is UnknownHostException -> {
            UnresolvedAddressException()
        }

        is ApolloHttpException -> {
            HttpException(httpStatusCode = statusCode)
        }

        is ApolloNetworkException -> {
            UnresolvedAddressException()
        }

        else -> this
    }
}
