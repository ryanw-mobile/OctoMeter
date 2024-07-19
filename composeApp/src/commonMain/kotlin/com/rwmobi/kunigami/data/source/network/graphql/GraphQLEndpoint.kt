/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.Query
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import com.rwmobi.kunigami.graphql.type.ObtainJSONWebTokenInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class GraphQLEndpoint(
    private val apolloClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val defaultPageSize = 100

    suspend fun getEnergyProducts(
        postcode: String,
        afterCursor: String? = null,
        pageSize: Int = defaultPageSize,
    ): EnergyProductsQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = EnergyProductsQuery(
                    postcode = postcode,
                    pageSize = pageSize,
                    afterCursor = afterCursor?.let { Optional.present(it) } ?: Optional.absent(),
                ),
            )
        }
    }

    suspend fun getSingleEnergyProduct(productCode: String, postcode: String): SingleEnergyProductQuery.Data {
        return withContext(dispatcher) {
            runQuery(query = SingleEnergyProductQuery(productCode = productCode, postcode = postcode))
        }
    }

    suspend fun getAuthorizationToken(apiKey: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(APIKey = Optional.present(apiKey))
        return obtainKrakenToken(input = input)
    }

    suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(refreshToken = Optional.present(refreshToken))
        return obtainKrakenToken(input = input)
    }

    private suspend fun <D : Query.Data> runQuery(query: Query<D>): D {
        val response = apolloClient.query(query).execute()

        return response.data ?: run {
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
}
