/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
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

    suspend fun getAuthorizationToken(apiKey: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(APIKey = Optional.present(apiKey))
        return obtainKrakenToken(input = input)
    }

    suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(refreshToken = Optional.present(refreshToken))
        return obtainKrakenToken(input = input)
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
