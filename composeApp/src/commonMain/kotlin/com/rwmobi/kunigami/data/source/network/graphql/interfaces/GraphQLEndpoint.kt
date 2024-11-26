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

package com.rwmobi.kunigami.data.source.network.graphql.interfaces

import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint.Constants.DEFAULT_PAGE_SIZE
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.PropertiesQuery
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import com.rwmobi.kunigami.graphql.SmartMeterTelemetryQuery
import kotlinx.datetime.Instant

interface GraphQLEndpoint {
    object Constants {
        const val DEFAULT_PAGE_SIZE = 100
    }

    /***
     * Authorization Token not required.
     */
    suspend fun getEnergyProducts(
        postcode: String,
        afterCursor: String? = null,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): EnergyProductsQuery.Data

    /***
     * Authorization Token not required.
     */
    suspend fun getSingleEnergyProduct(
        productCode: String,
        postcode: String,
        afterCursor: String? = null,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): SingleEnergyProductQuery.Data

    suspend fun getSmartMeterTelemetry(
        meterDeviceId: String,
        start: Instant,
        end: Instant,
    ): SmartMeterTelemetryQuery.Data

    /***
     * The GraphQL Account query can't return everything we need. Underlying we call PropertiesQuery in this implementation.
     * Authorization Token required.
     */
    suspend fun getAccount(
        accountNumber: String,
    ): PropertiesQuery.Data

    //region Token Management
    suspend fun getAuthorizationToken(apiKey: String): Result<Token>

    suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token>
}
