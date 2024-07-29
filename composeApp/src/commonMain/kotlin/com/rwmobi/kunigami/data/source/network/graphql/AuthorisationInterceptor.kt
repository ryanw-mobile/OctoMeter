/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
