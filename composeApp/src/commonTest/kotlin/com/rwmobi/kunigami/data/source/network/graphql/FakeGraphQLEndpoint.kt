/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.graphql

import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.PropertiesQuery
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import kotlinx.datetime.Clock

class FakeGraphQLEndpoint : GraphQLEndpoint {
    override suspend fun getEnergyProducts(postcode: String, afterCursor: String?, pageSize: Int): EnergyProductsQuery.Data {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleEnergyProduct(productCode: String, postcode: String, afterCursor: String?, pageSize: Int): SingleEnergyProductQuery.Data {
        TODO("Not yet implemented")
    }

    override suspend fun getAccount(accountNumber: String): PropertiesQuery.Data {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthorizationToken(apiKey: String): Result<Token> {
        return Result.success(
            Token(
                token = "newToken",
                expiresIn = Clock.System.now().epochSeconds + 3600,
                refreshToken = "refreshToken",
                refreshExpiresIn = Clock.System.now().epochSeconds + 7200,
            ),
        )
    }

    override suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token> {
        return Result.success(
            Token(
                token = "refreshedToken",
                expiresIn = Clock.System.now().epochSeconds + 3600,
                refreshToken = "refreshToken",
                refreshExpiresIn = Clock.System.now().epochSeconds + 7200,
            ),
        )
    }
}
