/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.graphql.interfaces

import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint.Constants.DEFAULT_PAGE_SIZE
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.PropertiesQuery
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery

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
