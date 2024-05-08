/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.source.network

import com.rwmobi.roctopus.data.source.network.dto.ConsumptionApiResponse
import com.rwmobi.roctopus.data.source.network.extensions.encodeApiKey
import com.rwmobi.roctopus.data.source.network.model.ConsumptionGrouping
import com.rwmobi.roctopus.data.source.network.model.ConsumptionOrdering
import com.rwmobi.roctopus.domain.extensions.formatInstantWithoutSeconds
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class ElectricityMeterPointsEndpoint(
    baseUrl: String,
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val endpointUrl = "$baseUrl/v1/electricity-meter-points"

    /**
     * For billing, consumption needs to call roundToNearestEvenHundredth(). See comments there.
     */
    suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        pageSize: Int? = null,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
        orderBy: ConsumptionOrdering = ConsumptionOrdering.LATEST_FIRST,
        groupBy: ConsumptionGrouping = ConsumptionGrouping.HALF_HOURLY,
    ): ConsumptionApiResponse? {
        return withContext(dispatcher) {
            httpClient.get("$endpointUrl/$mpan/meters/$meterSerialNumber/consumption") {
                header("Authorization", "Basic ${encodeApiKey(apiKey)}")
                parameter("page_size", pageSize)
                parameter("period_from", periodFrom?.formatInstantWithoutSeconds())
                parameter("period_to", periodTo?.formatInstantWithoutSeconds())
                parameter("order_by", orderBy.apiValue)
                parameter("group_by", groupBy.apiValue)
            }.body()
        }
    }
}
