/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network

import com.rwmobi.kunigami.data.source.network.dto.AccountApiResponse
import com.rwmobi.kunigami.data.source.network.extensions.encodeApiKey
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AccountEndpoint(
    baseUrl: String,
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val endpointUrl = "$baseUrl/v1/accounts"

    /**
     * For billing, consumption needs to call roundToNearestEvenHundredth(). See comments there.
     */
    suspend fun getAccount(
        apiKey: String,
        accountNumber: String,
    ): AccountApiResponse? {
        return withContext(dispatcher) {
            val response = httpClient.get("$endpointUrl/$accountNumber") {
                header("Authorization", "Basic ${encodeApiKey(apiKey)}")
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    response.body() as AccountApiResponse?
                }

                HttpStatusCode.NotFound -> {
                    null
                }

                else -> {
                    throw Exception("Error: HTTP ${response.status}")
                }
            }
        }
    }
}
