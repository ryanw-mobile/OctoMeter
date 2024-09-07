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
