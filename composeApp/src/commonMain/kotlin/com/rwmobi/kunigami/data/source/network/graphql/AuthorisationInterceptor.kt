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

package com.rwmobi.kunigami.data.source.network.graphql

import androidx.compose.ui.util.fastAny
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.rwmobi.kunigami.domain.repository.TokenRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthorisationInterceptor(
    private val tokenRepository: TokenRepository,
) : HttpInterceptor {
    private val mutex = Mutex()

    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
        val requiredAuthorization = request.headers.fastAny { it.name == "Requires-Authorization" && it.value == "true" }

        if (!requiredAuthorization) {
            return chain.proceed(request)
        }

        var token = mutex.withLock {
            // get current token
            tokenRepository.getToken(forceRefresh = false)?.token
        }

        if (token == null) {
            return HttpResponse.Builder(statusCode = 401).build()
        }

        val response = chain.proceed(request.newBuilder().addHeader("Authorization", token).build())

        return if (response.statusCode == 401) {
            token = mutex.withLock {
                // get new token
                tokenRepository.getToken(forceRefresh = true)?.token
            }

            if (token == null) {
                return HttpResponse.Builder(statusCode = 401).build()
            }
            chain.proceed(request.newBuilder().addHeader("Authorization", token).build())
        } else {
            response
        }
    }
}
