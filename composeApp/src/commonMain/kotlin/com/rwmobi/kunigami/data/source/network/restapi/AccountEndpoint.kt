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

package com.rwmobi.kunigami.data.source.network.restapi

import com.rwmobi.kunigami.data.source.network.dto.account.AccountApiResponse
import com.rwmobi.kunigami.data.source.network.extensions.encodeApiKey
import com.rwmobi.kunigami.domain.exceptions.HttpException
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
                    throw HttpException(response.status.value)
                }
            }
        }
    }
}
