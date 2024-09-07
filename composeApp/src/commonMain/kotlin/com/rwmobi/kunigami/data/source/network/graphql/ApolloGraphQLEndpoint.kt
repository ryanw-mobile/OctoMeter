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

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
import com.rwmobi.kunigami.graphql.PropertiesQuery
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import com.rwmobi.kunigami.graphql.type.ObtainJSONWebTokenInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ApolloGraphQLEndpoint(
    private val apolloClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GraphQLEndpoint {
    /***
     * Authorization Token not required.
     */
    override suspend fun getEnergyProducts(
        postcode: String,
        afterCursor: String?,
        pageSize: Int,
    ): EnergyProductsQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = EnergyProductsQuery(
                    postcode = postcode,
                    pageSize = pageSize,
                    afterCursor = afterCursor?.let { Optional.present(it) } ?: Optional.absent(),
                ),
                requireAuthentication = false,
            )
        }
    }

    /***
     * Authorization Token not required.
     */
    override suspend fun getSingleEnergyProduct(
        productCode: String,
        postcode: String,
        afterCursor: String?,
        pageSize: Int,
    ): SingleEnergyProductQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = SingleEnergyProductQuery(
                    productCode = productCode,
                    postcode = postcode,
                    pageSize = pageSize,
                    afterCursor = afterCursor?.let { Optional.present(it) } ?: Optional.absent(),
                ),
                requireAuthentication = false,
            )
        }
    }

    /***
     * The GraphQL Account query can't return everything we need. Underlying we call PropertiesQuery in this implementation.
     * Authorization Token required.
     */
    override suspend fun getAccount(
        accountNumber: String,
    ): PropertiesQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = PropertiesQuery(
                    accountNumber = accountNumber,
                ),
                requireAuthentication = true,
            )
        }
    }

    //region Token Management
    override suspend fun getAuthorizationToken(apiKey: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(APIKey = Optional.present(apiKey))
        return obtainKrakenToken(input = input)
    }

    override suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(refreshToken = Optional.present(refreshToken))
        return obtainKrakenToken(input = input)
    }

    private suspend fun <D : Query.Data> runQuery(query: Query<D>, requireAuthentication: Boolean): D {
        // AuthorisationInterceptor will manage token insertion
        val response = if (requireAuthentication) {
            apolloClient
                .query(query)
                .addHttpHeader("Requires-Authorization", "true")
                .execute()
        } else {
            apolloClient.query(query).execute()
        }

        response.exception?.let { exception ->
            exception.printStackTrace()
            throw exception
        }

        response.errors?.let {
            // Handle GraphQL errors
            throw ApolloGraphQLException(it)
        }

        response.data?.let {
            return it
        } ?: run {
            throw IllegalStateException("Unexpected GraphQL Error")
        }
    }

    private suspend fun obtainKrakenToken(input: ObtainJSONWebTokenInput): Result<Token> {
        return withContext(dispatcher) {
            runCatching {
                val response = apolloClient.mutation(ObtainKrakenTokenMutation(input)).execute()

                response.data?.obtainKrakenToken?.let {
                    // Handle (potentially partial) data
                    Token.fromObtainKrakenToken(obtainKrakenToken = it)
                } ?: run {
                    // Something wrong happened
                    response.exception?.let {
                        // Handle fetch errors
                        it.printStackTrace()
                        throw it
                    } ?: run {
                        // Handle GraphQL errors in response.errors
                        val concatenatedMessages = response.errors?.joinToString(separator = ",") { it.message }
                        throw IllegalStateException("Unhandled response errors: $concatenatedMessages")
                    }
                }
            }
        }.except<CancellationException, _>()
    }
    //endregion
}
